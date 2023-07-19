import React from 'react';
import { useState, useEffect } from 'react';
import * as API from './../../constants/api_config'
import './Customer.css'
export function CustomerDetail() {
    const [customer, setCustomer] = useState([])

    useEffect(() => {
        var arr = window.location.href.split("/")
        const url = API.DOMAIN + API.CUSTOMER + arr[arr.length - 1]
        fetch(url, {
            method: 'GET',
            credentials: "same-origin"
        })
            .then(response => {
                return response.json()
            })
            .then(data => {
                if (data.code === 200) {
                    setCustomer(data.data)
                } else {
                    alert("User không tìm thấy")
                    return
                }
            })
            .catch((error) => {
                alert("ERROR")
                return
            });
    }, [])
    return (
        <>
            {
                customer == undefined ? (<></>) :
                    (<>

                        <div className="wrapper rounded-lg shadow-lg text-base">
                            <div className="left flex flex-col items-center justify-start space-y-8">
                                {
                                    customer.gender == 1 ? (<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7Mt-bw3ab6MmhmZ1ruSICOUPn2atHBmhmJHYTx7Cy3hIhsCKTSWLMh_XdrhFUjo3CN9U&usqp=CAU" alt="user" width="400"></img>) 
                                    : (<img src="https://img.freepik.com/premium-vector/young-woman-face-cartoon_18591-44461.jpg?w=2000" alt="user" width="400"></img>)
                                }
                                
                                <h2 className='text-4xl'>{customer.name}</h2>
                            </div>
                            <div className="right w-full flex flex-col space-y-16">
                                <div className="info flex flex-col space-y-8">
                                    <h2 className='text-4xl w-full bg-slate-100 p-2 shadow-md'>Thông tin cơ bản</h2>
                                    <div className="info_data text-xl">
                                        <div className="data">
                                            <h3>Email</h3>
                                            <p>{customer.email}</p>
                                        </div>

                                        <div className="data">
                                            <h3>Số điện thoại</h3>
                                            <p>{customer.phoneNumber}</p>
                                        </div>
                                    </div>
                                    <div className="info_data text-xl">
                                        <div className="data">
                                            <h3>Ngày sinh</h3>
                                            <p>{customer.birthday}</p>
                                        </div>

                                        <div className="data">
                                            <h3>Giới tính</h3>
                                            {customer.gender == 1 ? (<p>Nam</p>) : (<p>Nữ</p>)}
                                        </div>

                                        <div className="data">
                                            <h3>Địa chỉ</h3>
                                            <p>{customer.province}</p>
                                        </div>

                                    </div>
                                </div>

                                <div className="info text-xl flex flex-col space-y-8">
                                    <h2 className='text-4xl w-full bg-slate-100 p-2 shadow-md'>Thông tin bổ sung</h2>
                                    <div className="projects_data">
                                        <div className="data">
                                            <h3>Thể loại ưu thích</h3>
                                            <p>Ngắn hạn: {customer.shortHobbies}</p>
                                            <p>Dài hạn: {customer.hobbies }</p>
                                        </div>
                                        <div className="data">
                                            <h3>Giá trị</h3>
                                            <p>Số sách đã từng xem: {customer.totalBookView}</p>
                                            <p>Trung bình giá trị sách đã xem: {customer.avgBookValue}</p>
                                            <p>Trung bình giá trị đơn hàng: {customer.avgBillValue}</p>
                                            <p>Giá trị đơn hàng tối thiểu: {customer.minBillValue}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </>)
            }
        </>
    );
}