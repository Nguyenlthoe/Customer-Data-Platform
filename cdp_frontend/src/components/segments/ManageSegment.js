import React from 'react';
import { useState, useEffect } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'
import { Button } from '@mui/material'

import './Segment.css'
var nextPage = false;
var prevPage = false;
export function ManageSegment() {
    const [page, setPage] = useState(0)
    const [total, setTotal] = useState(0)
    const [segments, setSegments] = useState([])

    useEffect(() => {
        handleUpdatePage(0)
    }, [])

    function handleUpdatePage(index) {
        console.log(index)
        const url = API.DOMAIN + API.SEGMENT + "?page=" + index
        fetch(url, {
            method: 'GET',
            credentials: "same-origin"
        })
            .then(response => {
                return response.json()
            })
            .then(data => {
                if (data.code === 200) {
                    setTotal(data.data.totalElements)
                    setSegments(data.data.segments)
                    if (index > 0) {
                        prevPage = true
                    } else {
                        prevPage = false
                    }
                    if (index + 1 == data.data.totalPages) {
                        nextPage = false;
                    } else {
                        nextPage = true;
                    }
                } else {
                    alert("Segment không tìm thấy")
                }
            })
            .catch((error) => {
                alert("ERROR")
            });
    }

    function handleNext() {
        const next = page + 1
        setPage(next)
        handleUpdatePage(next)
    }

    function handlePrev() {
        const prev = page - 1
        setPage(prev)
        handleUpdatePage(prev)
    }

    function handleViewDetail(index) {
        window.location.href = "/segment/" + index
    }

    function handleUpdate(index) {
        window.location.href = "/admin/modifysegment/" + index
    }

    return (
        <div className="m-4 p-4 min-h-[90vh] shadow-lg bg-white rounded-xl flex flex-col items-center justify-start space-y-10">
            <div className="overview">
                <label className="text-4xl">Tổng số phân khúc trong hệ thống: {total}</label>
            </div>
            <div className='w-full flex flex-col items-center justify-center space-y-4'>
                {/* <div className="separate"></div> */}
                <div className="my_table">
                    <table className="text-xl shadow-lg">
                        <thead>
                            <tr className='bg-slate-200 w-full border p-2'>
                                <th className='p-2'>ID</th>
                                <th className='p-2'>Tiêu đề</th>
                                <th className='p-2'>Số điều kiện</th>
                                <th className='p-2'>Thời gian tạo</th>
                                <th className='p-2'>Thời gian cập nhật</th>
                                <th className='p-2'></th>
                                <th className='p-2'></th>
                            </tr>
                        </thead>

                        <tbody className='text-lg'>
                            {
                                segments.map((segment) => {
                                    return (
                                        <tr key={segment.segmentId} className='border hover:bg-slate-50 !h-[120px] overflow-auto'>
                                            <td className='p-2 border' style={{ width: '5em' }}>{segment.segmentId}</td>
                                            <td className='p-2 border' style={{ width: '45em' }}>{segment.name}</td>
                                            <td className='p-2 border' style={{ width: '10em' }}>  {segment.conditions.length}</td>
                                            <td className='p-2 border' style={{ width: '15em' }}>{segment.createdAt}</td>
                                            <td className='p-2 border' style={{ width: '15em' }}>{segment.updatedAt}</td>
                                            <td className='p-2' style={{ width: '7em' }}><Button variant='outlined' className="w-24" onClick={() => { handleViewDetail(segment.segmentId) }}>Chi tiết</Button></td>
                                            <td className='p-2' style={{ width: '7em' }}><Button variant='outlined' className="w-24" onClick={() => { handleUpdate(segment.segmentId) }}>Cập nhật</Button></td>
                                        </tr>
                                    )
                                })
                            }
                        </tbody>
                    </table>
                </div>
                {/* <div className="separate"></div> */}
            </div>
            <div className='flex justify-between space-x-2'>
                {(prevPage && <Button className="w-36" variant='contained' onClick={handlePrev}>Trang trước</Button>)
                    || <Button className="w-36" disabled variant='contained' onClick={handlePrev}>Trang trước</Button>}
                {(nextPage && <Button className="w-36" variant='contained' onClick={handleNext}>Trang sau</Button>)
                    || <Button className="w-36" disabled variant='contained' onClick={handleNext}>Trang sau</Button>}
            </div>
        </div>
    )
}