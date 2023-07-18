import React from 'react';
import { useState, useEffect } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'

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
                    if(index > 0){
                        prevPage = true
                    } else {
                        prevPage = false
                    }
                    if(index + 1 == data.data.totalPages){
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

    function handleViewDetail(index){
        window.location.href = "/segment/" + index
    }

    function handleUpdate(index){
        window.location.href = "/admin/modifysegment/" + index
    }

    return (
        <>
            <div className="overview">
                <label className="title_1">Tổng số phân khúc trong hệ thống: {total}</label>
            </div>
            <div className="separate"></div>
            <div className="my_table">
                <table className="">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tiêu đề</th>
                            <th>Số điều kiện</th>
                            <th>Thời gian tạo</th>
                            <th>Thời gian cập nhật</th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        {
                            segments.map((segment) => {
                                return (
                                    <tr key={segment.segmentId}>
                                        <td style={{width: '5em'}}>{segment.segmentId}</td>
                                        <td style={{width: '45em'}}>{segment.name}</td>
                                        <td style={{width: '10em'}}>  {segment.conditions.length}</td>
                                        <td style={{width: '15em'}}>{segment.createdAt}</td>
                                        <td style={{width: '15em'}}>{segment.updatedAt}</td>
                                        <td style={{width: '7em'}}><button className="btn btn_green" onClick={() => {handleViewDetail(segment.segmentId)}}>Chi tiết</button></td>
                                        <td style={{width: '7em'}}><button className="btn btn_green" onClick={() => {handleUpdate(segment.segmentId)}}>Cập nhật</button></td>
                                    </tr>
                                )
                            })
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