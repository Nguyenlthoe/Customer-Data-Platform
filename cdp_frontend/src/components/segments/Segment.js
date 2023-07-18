import React from 'react';
import { useState, useEffect } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'

import './Segment.css'

var nextPage = false;
var prevPage = false;

export function SegmentDetail() {
    const [page, setPage] = useState(0)
    const [total, setTotal] = useState(0)
    const [segment, setSegment] = useState()
    const [customers, setCustomers] = useState([])
    useEffect(() => {
        var arr = window.location.href.split("/")
        const url = API.DOMAIN + API.SEGMENT + "/" + arr[arr.length - 1]
        fetch(url, {
            method: 'GET',
            credentials: "same-origin"
        })
            .then(response => {
                return response.json()
            })
            .then(data => {
                if (data.code === 200) {
                    setSegment(data.data)
                    console.log(segment)
                    handleUpdateListCustomer(0)
                } else {
                    alert("Segment không tìm thấy")
                    return
                }
            })
            .catch((error) => {
                alert("ERROR")
                return
            });
    }, [])

    function handleNext() {
        const next = page + 1
        setPage(next)
        handleUpdateListCustomer(next)
    }

    function handlePrev() {
        const prev = page - 1
        setPage(prev)
        handleUpdateListCustomer(prev)
    }


    function handleUpdateListCustomer(page_index) {
        var arr = window.location.href.split("/")
        const url = API.DOMAIN + API.SEGMENT_PAGE + arr[arr.length - 1] + "?pageId=" + page_index
        fetch(url, {
            method: 'GET',
            credentials: "same-origin"
        })
            .then(response => {
                return response.json()
            })
            .then(data => {
                if (data.code === 200) {
                    setCustomers(data.data.customers)
                    setTotal(data.data.totalElements)

                    if (page_index > 0) {
                        prevPage = true
                    } else {
                        prevPage = false
                    }
                    if (page_index + 1 == data.data.totalPages) {
                        nextPage = false;
                    } else {
                        nextPage = true;
                    }

                } else {
                    alert("Segment không tìm thấy")
                    return
                }
            })
            .catch((error) => {
                alert("ERROR")
                return
            });
    }


    function handleViewDetail(index){
        window.location.href = "/customer/" + index
    }

    return (
        <>
            <div className='space_50'></div>
            <div className='overview'>
                {
                    segment != undefined ? (
                    
                    <div className='overview left_content'>
                        <label className='title_2 margin_left20 margin_top20'>Tên phân khúc: {segment.name}</label>
                        <div className='space_20'></div>
                        <label className='title_2 margin_left20 margin_top20'>Thời gian khởi tạo: {segment.createdAt}</label>
                        <div className='space_20'></div>
                        <label className='title_2 margin_left20 margin_top20'>Thời gian cập nhật: {segment.updatedAt}</label>
                        <div className='space_20'></div>
                        <label className="title_2 margin_left20 margin_top20">Số  khách hàng trong phân khúc: {total}</label>
                    </div>
                    ) : (
                        <>
                        </>
                    )
                }
                
            </div>
            <div className='space_20'></div>
            <div className="separate"></div>
            <div className="my_table margin_left20">
                <table className="">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        {
                            customers != undefined ? (
                            customers.map((customer) => {
                                return (
                                    <tr key={customer.customerId}>
                                        <td style={{ width: '5em' }}>{customer.userId}</td>
                                        <td style={{ width: '30em' }}>{customer.name}</td>
                                        <td style={{ width: '30em' }}>  {customer.email}</td>
                                        <td style={{ width: '30em' }}>{customer.phoneNumber}</td>
                                        <td style={{ width: '5em' }}><button className="btn btn_green" onClick={() => {handleViewDetail(customer.userId)}}>Chi tiết</button></td>
                                    </tr>
                                )
                            }) ) : (<></>)
                        }
                    </tbody>
                </table>
            </div>
            <div className="separate"></div>
            <div className='page'>
                {(prevPage && <button className="btn" onClick={handlePrev}>Trang trước</button>)
                    || <button disabled className="btn" onClick={handlePrev}>Trang trước</button>}
                {(nextPage && <button className="btn" onClick={handleNext}>Trang sau</button>)
                    || <button disabled className="btn" onClick={handleNext}>Trang sau</button>}
            </div>
        </>
    )
}
