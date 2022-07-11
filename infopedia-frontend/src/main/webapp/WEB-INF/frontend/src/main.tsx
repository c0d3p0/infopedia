import React from 'react'
import { CookiesProvider } from 'react-cookie';
import ReactDOM from 'react-dom/client'
import { Provider } from 'react-redux'

import App from './components/App/App'
import store from './data/AppStore';

import 'bootstrap-icons/font/bootstrap-icons.css';
import './index.css'


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <CookiesProvider>
      <Provider store={store}>
        <App />
      </Provider>
    </CookiesProvider>
  </React.StrictMode>
)
