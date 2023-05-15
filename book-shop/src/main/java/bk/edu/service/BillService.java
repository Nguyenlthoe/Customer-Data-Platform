package bk.edu.service;

import bk.edu.data.entity.*;
import bk.edu.data.mapper.BillMapper;
import bk.edu.data.req.BillRequest;
import bk.edu.data.response.dto.BillDto;
import bk.edu.exception.BillRequestInvalid;
import bk.edu.exception.CartRequestInvalid;
import bk.edu.repository.BillRelationRepository;
import bk.edu.repository.BillRepository;
import bk.edu.repository.CartRepository;
import bk.edu.repository.UserRepository;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {
    @Autowired
    BillMapper  billMapper;

    @Autowired
    BillRepository billRepository;

    @Autowired
    BillRelationRepository billRelationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    public List<BillDto> getBills(Integer userId, Integer status) {
        UserEntity user = userRepository.findByUserId(userId);

        if(user == null){
            throw new CartRequestInvalid("User not found");
        }

        List<BillEntity> bills = billRepository.findAllByUserAndStatus(user, status);
        List<BillDto> billDtoList = billMapper.listBillEntityToDto(user,bills);
        return billDtoList;
    }

    public void createBill(BillRequest billRequest, Integer userId) {
        UserEntity user = userRepository.findByUserId(userId);

        if(user == null){
            throw new CartRequestInvalid("User not found");
        }
        List<CartEntity> cartEntities = cartRepository.findAllByUser(user);
        if(cartEntities.size() == 0){
            throw new BillRequestInvalid("Cart don't have any item");
        }

        BillEntity billEntity = billMapper.billRequestToEntity(billRequest, user);
        billRepository.saveAndFlush(billEntity);
        cartEntities.forEach(cartEntity -> {
            BillRelationKey billRelationKey = new BillRelationKey(billEntity.getBillId(), cartEntity.getBook().getBookId());
            BillRelation billRelation = new BillRelation(billRelationKey, billEntity,  cartEntity.getBook(), cartEntity.getQuantity());
            billRelationRepository.saveAndFlush(billRelation);
        });

        Iterable<CartEntity> carts = new IteratorIterable<>(cartEntities.iterator());
        cartRepository.deleteAll(carts);
    }
}
