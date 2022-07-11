import { useEffect } from 'react';
import { Routes, Route, useLocation, useNavigate, NavigateFunction } from 'react-router-dom';

import Footer from '../Footer/Footer';
import Header from '../Header/Header';
import InvalidUrl from '../InvalidUrl/InvalidUrl';
import LoginForm from '../LoginForm/LoginForm';
import UserArticles from '../UserArticles/UserArticles';
import About from '../About/About';
import ArticleList from '../ArticleList/ArticleList';
import ArticlePage from '../ArticlePage/ArticlePage';
import SignUpForm from '../SignUpForm/SignUpForm';
import SideMenu from '../SideMenu/SideMenu';
import UserForm from '../UserForm/UserForm';
import ArticleForm from '../ArticleForm/ArticleForm';
import RemoveAccountForm from '../RemoveAccountForm/RemoveAccountForm';
import SubContentForm from '../SubContentForm/SubContentForm';
import UserList from '../UserList/UserList';
import AccessForbidden from '../AccessForbidden/AccessForbidden';


import './App.css';


export default function AppView(props: IProps) {
  const location = useLocation();
  const navigate = useNavigate();
  useEffect(() => props.redirectIfNoPermission(navigate), [location]);

  return (
    <div className="app">
      <Header />
      <div className="page-content">
        <SideMenu />
        <Routes>
          <Route path="/" element={<About />} />
          <Route path="/index" element={<About />} />
          <Route path="/about" element={<About />} />
          <Route path="/articles/*" element={<ArticleList />} />
          <Route path="/article-page/*" element={<ArticlePage />} />
          <Route path="/login-form" element={<LoginForm />} />
          <Route path="/sign-up-form" element={<SignUpForm />} />

          <Route path="/my-profile/*" element={<UserForm />} />
          <Route path="/my-articles/*" element={<UserArticles />} />
          <Route path="/my-article/*" element={<ArticleForm />} />
          <Route path="/my-article-sub-content/*" element={<SubContentForm />} />
          <Route path="/add-article" element={<ArticleForm />} />
          <Route path="/remove-my-account" element={<RemoveAccountForm />} />

          <Route path="/users/*" element={<UserList />} />
          <Route path="/add-user" element={<UserForm />} />
          <Route path="/edit-user/*" element={<UserForm />} />
          <Route path="/edit-article/*" element={<ArticleForm />} />
          <Route path="/edit-article-sub-content/*" element={<SubContentForm />} />
          <Route path="/access-forbidden/*" element={<AccessForbidden />} />
          <Route path="/*" element={<InvalidUrl />} />
        </Routes>
      </div>
      <Footer />
    </div>
  )
}


interface IProps {
  redirectIfNoPermission(navigate: NavigateFunction): void;
}