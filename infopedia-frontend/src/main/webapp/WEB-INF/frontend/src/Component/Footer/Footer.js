import React from 'react';

import './Footer.css';


export default function Footer()
{
  let appVersion = "Version 0.1, \u00A9 2020";

  return (
    <footer className="Footer RowLinedFlex">
      <label>{appVersion}</label>
    </footer>
  );
}