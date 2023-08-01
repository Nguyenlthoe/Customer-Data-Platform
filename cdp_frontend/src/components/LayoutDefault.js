import React from 'react';
import {Body} from './Body';
import { Header } from './header/header';
import { Footer } from './footer/footer';
import { Helmet } from "react-helmet";
export function LayoutDefault() {
  return (
    <div className="App">
      <Helmet>
        <title>CDP</title>
        <script src="https://kit.fontawesome.com/19b20c95d0.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://fontawesome.com/icons/trash?f=classic&s=solid&pc=%23e65151"></link>
        <link rel="stylesheet" href="https://fontawesome.com/v5/icons/edit?f=classic&s=solid&pc=%23fbe032"></link>
        <link rel="stylesheet" href="https://fontawesome.com/icons/circle-info?f=classic&s=solid&pc=%233c82fb"></link>
        <meta name="description" content="My app description" />
      </Helmet>
      <Header></Header>
      <Body/>
      <Footer/>
    </div>
  )
}