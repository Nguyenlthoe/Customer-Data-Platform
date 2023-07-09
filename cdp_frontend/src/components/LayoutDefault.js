import React from 'react';
import {Body} from './Body';
import { Header } from './header/header';
import { Footer } from './footer/footer';
export function LayoutDefault() {
  return (
    <div className="App">
      <Header></Header>
      <Body/>
      <Footer/>
    </div>
  )
}