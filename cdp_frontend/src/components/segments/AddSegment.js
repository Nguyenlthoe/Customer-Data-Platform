import React from 'react';
import { useState } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'
import './Segment.css'
import { Button } from '@mui/material';
var conditionSize = 0;
export function AddSegment(){
    const [conditions, setConditions] = useState([])
    const [title, setTitle] = useState("")
    function handleAddCondition() {
        const newCondition = {
            id: conditionSize,
            field: "",
            operator: "",
            type: "",
            value: ""
        }
        let cp = conditions.slice()
        cp.push(newCondition)
        setConditions(cp)
        conditionSize =  conditionSize + 1
    }

    function handleRemoveCondition(index) {
        let cp = conditions.slice();
        let item = null
        cp.forEach((condition) => {
            if(condition.id == index){
                item = condition
            }
        })
        const indexR = cp.indexOf(item)
        console.log(cp)
        cp.splice(indexR, 1);
        console.log(cp)
        setConditions(cp)
    }

    function handleAddSegment(){
        const listCondition = []
        for (let i = 0; i < conditionSize; i++) {
            const condition = document.getElementById(i);
            if(condition != null){
                const operator_option = document.getElementById("operator_option" + i)
                const operator = operator_option.options[operator_option.selectedIndex].value;
                const type_option = document.getElementById("type_option" + i)
                const type = type_option.options[type_option.selectedIndex].value;
                const field = condition.firstChild.value
                const value = document.getElementById("value" + i).value
                listCondition.push({
                    field: field,
                    operator: operator,
                    type: type,
                    value: value
                })
            }
        }

        const url = API.DOMAIN + API.SEGMENT;
        fetch(url, {
            method: 'POST',
            headers: {
                'accept': '*/*',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: title,
                conditions: listCondition
            }),
            credentials: "same-origin"
        })
            .then(response => {
                return response.json()
            })
            .then(data => {
                
                console.log(data)
                if (data.code === 200) {
                    alert("Tạo mới phân khúc thành công");
                    window.location.href = "/segments"
                } else {
                    alert("Tạo mới thất bại")
                    console.log(data)
                }
            })
            .catch((error) => {
                //alert("ERROR");
                console.log(error)
            });
    }

    return (
        <>
            <div className="m-4 p-4 min-h-[90vh] shadow-lg bg-white rounded-xl flex flex-col items-center justify-start space-y-10">
            <div className="flexbox_center">
                <label className="title_1 text-4xl">Thêm phân khúc</label>
            </div>
            <div className='flexbox_spacearound m-4 p-4'>
                <form action="" method="POST" className="container_custom border_red" >
                    <div className="flexbox_inline">
                        <div className="flex_3 flex_column text-base shadow-xl">
                            <div className="flexbox_spacearound margin_left20 py-4">
                                <input className="title_input margin_left20" type="text" name="title" id="" placeholder='Tên phân khúc'
                                    value={title} required
                                    onChange={(e) => setTitle(e.target.value)} />
                            </div>
                            <div id="conditions_input margin_left20">
                                {conditions.map((condition) => (
                                    <div className="flexbox_spacearound py-4" id={condition.id} key={condition.id}>
                                        <input className="item_flex3 margin_left20 title_input" type="text" name="title" id="" placeholder='Trường'
                                        />
                                        <div className="flexbox_horizol">
                                            <label className='label_option'>Chọn toán tử :</label>
                                            <select id={"operator_option" + condition.id} name="operator">
                                                {CONFIG.operators.map( (operator) => (
                                                    <option id = {operator + condition.id} key={operator + condition.id} name={operator} value={operator}>{operator}</option>
                                                )
                                                )}
                                            </select>
                                        </div>
                                        <div className="flexbox_horizol">
                                            <label className='label_option'>Chọn kiểu giá trị:</label>
                                            <select id={"type_option" + condition.id} name="type">
                                                {CONFIG.types.map( (type) => (
                                                    <option id = {type + condition.id} key={type + condition.id} name={type} value={type}>{type}</option>
                                                )
                                                )}
                                            </select>
                                        </div>
                                        <input type="text" id={"value" + condition.id} className="item_flex7 margin_left20 title_input" name="value" placeholder='Giá trị'
                                        />
                                        {<button type="button" className=" btn_height30 btn !w-40 !mx-4" onClick={() => handleRemoveCondition(condition.id)}>Xoá điều kiện</button>}
                                    </div>
                                ))}
                            </div>
                            <div className='margin_top20'></div>
                        </div>
                    </div>
                </form>
            </div>
            <div className='flexbox_spacearound flex w-full items-start justify-start space-x-2 ml-8'>
                                <Button variant='contained' className="w-56 text-xl" onClick={handleAddCondition}>Thêm điều kiện</Button>
                                <Button variant='contained' className="w-56 text-xl" onClick={handleAddSegment}>Tạo phân khúc</Button>
                            </div>
            </div>
        </>
    )
}