import React from 'react';
import { useState, useEffect } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'

import './Segment.css'
import { Button } from '@mui/material';

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


    function handleViewDetail(index) {
        window.location.href = "/customer/" + index
    }

    return (
        <>
            <div className="m-4 p-4 shadow-lg bg-white rounded-xl flex flex-col items-start justify-start space-y-10">
                <div className='overview text-3xl'>
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
            </div>
            <div className="m-4 p-8 shadow-lg bg-white rounded-xl flex flex-col items-start justify-start space-y-10">
                <table className="text-xl shadow-lg w-full">
                    <thead>
                        <tr className='bg-slate-200 w-full border p-2'>
                            <th className='p-2'>ID</th>
                            <th className='p-2'>Tên</th>
                            <th className='p-2'>Email</th>
                            <th className='p-2'>Số điện thoại</th>
                            <th className='p-2'></th>
                        </tr>
                    </thead>

                    <tbody className='text-lg'>
                        {
                            customers != undefined ? (
                                customers.map((customer) => {
                                    return (
                                        <tr key={customer.customerId} className='border hover:bg-slate-50 !h-[120px] overflow-auto'>
                                            <td className='p-2 min-h-[200px] border' style={{ width: '5em' }}>{customer.userId}</td>
                                            <td className='p-2 min-h-[200px] border' style={{ width: '30em' }}>{customer.name}</td>
                                            <td className='p-2 min-h-[200px] border' style={{ width: '30em' }}>  {customer.email}</td>
                                            <td className='p-2 min-h-[200px] border' style={{ width: '30em' }}>{customer.phoneNumber}</td>
                                            <td className='p-2 min-h-[200px] border' style={{ width: '5em' }}><Button variant='outlined' className="" onClick={() => { handleViewDetail(customer.userId) }}>Chi tiết</Button></td>
                                        </tr>
                                    )
                                })) : (<></>)
                        }
                    </tbody>
                </table>
                <div className='flex w-full items-center justify-center space-x-2'>
                    {(prevPage && <Button variant='contained' className="w-36" onClick={handlePrev}>Trang trước</Button>)
                        || <Button variant='contained' disabled className="w-36" onClick={handlePrev}>Trang trước</Button>}
                    {(nextPage && <Button variant='contained' className="w-36" onClick={handleNext}>Trang sau</Button>)
                        || <Button variant='contained' disabled className="w-36" onClick={handleNext}>Trang sau</Button>}
                </div>
            </div>
        </>
    )
}
