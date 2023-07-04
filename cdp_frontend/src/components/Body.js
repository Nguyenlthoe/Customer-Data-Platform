import { Routes, Route } from 'react-router-dom';
import { AddSegment } from './segments/AddSegment';
import React from 'react';

export function Body(props) {

    return(
      <Routes>
          {/* <Route path="/segments" element = {<Segment/>}/>
          <Route path="/segment/:slug" element = {<SegmentDetail/>}/>
          <Route path="/admin/modifysegment/:slug" element = {<ModifySegment/>}/> */}
          <Route path="/admin/addsegment" element = {<AddSegment/>}/>
          {/* <Route path="/customers/:slug" element = {<Users/>}/>
          <Route path="/customer/:slug" element = {<UserDetail/>}/> */}
      </Routes>
    )
  }