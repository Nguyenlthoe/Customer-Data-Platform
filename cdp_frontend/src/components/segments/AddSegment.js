import React from 'react';
import { useState } from 'react';
import * as API from './../../constants/api_config'
import * as CONFIG from './../../constants/config'
import './Segment.css'
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
        cp.splice(index, 1);
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

        const url = API.DOMAIN + API.ADD_SEGMENT;
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
                } else {
                    alert("Tạo mới thất bại")
                    console.log(data)
                }
            })
            .catch((error) => {
                alert("ERROR");
                console.log(error)
            });
    }

    return (
        <>
            <div className="flexbox_center">
                <label className="title_1">Thêm phân khúc</label>
            </div>
            <div className='flexbox_spacearound'>
                <form action="" method="POST" className="container_custom border_red" >
                    <div className="flexbox_inline">
                        <div className="flex_3 flex_column">
                            <div className="flexbox_spacearound margin_left20">
                                <input className="title_input" type="text" name="title" id="" placeholder='Tên phân khúc'
                                    value={title} required
                                    onChange={(e) => setTitle(e.target.value)} />
                            </div>
                            <div id="conditions_input margin_left20">
                                {conditions.map((condition) => (
                                    <div className="flexbox_spacearound" id={condition.id} key={condition.id}>
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
                                        {<button type="button" className=" btn_height30 btn" onClick={() => handleRemoveCondition(condition.id)}>Xoá điều kiện</button>}
                                    </div>
                                ))}
                            </div>
                            <div className='margin_top20'></div>
                        </div>
                    </div>
                </form>
            </div>
            <div className='flexbox_spacearound'>
                <button className="btn margin_top20" onClick={handleAddCondition}>Thêm điều kiện</button>
                <button className="btn margin_top20" onClick={handleAddSegment}>Tạo mới phân khúc</button>
            </div>
        </>
    )
}