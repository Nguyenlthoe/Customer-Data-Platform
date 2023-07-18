import { Routes, Route, Navigate } from 'react-router-dom';
import { AddSegment } from './segments/AddSegment';
import { ModifySegment } from './segments/ModifySegment';
import { ManageSegment } from './segments/ManageSegment';
import { SegmentDetail } from './segments/Segment';
import { CustomerDetail } from './customers/CustomerDetail';
import React from 'react';

export function Body(props) {

    return(
      <Routes>
        <Route path="/" element={<Navigate to="/segments" />}>
        </Route>
          <Route path="/segments" element = {<ManageSegment/>}/>
          <Route path="/segment/:slug" element = {<SegmentDetail/>}/>
          <Route path="/admin/modifysegment/:slug" element = {<ModifySegment/>}/>
          <Route path="/admin/addsegment" element = {<AddSegment/>}/>
          <Route path="/customer/:slug" element = {<CustomerDetail/>}/>
      </Routes>
    )
  }