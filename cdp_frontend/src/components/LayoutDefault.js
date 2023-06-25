import {Body} from './Body'
import { Header } from './header/Header';
import {Footer} from './footer/Footer';
import React from 'react';
export function LayoutDefault() {
  return (
    <div className="App">
      <Header></Header>
      <Body/>
      <Footer/>
    </div>
  )
}