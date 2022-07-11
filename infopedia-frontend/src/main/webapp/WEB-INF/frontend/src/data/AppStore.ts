import { configureStore } from "@reduxjs/toolkit";

import currentArticleReducer from "../features/CurrentArticle";
import currentUserReducer from "../features/CurrentUser";


const store = configureStore({
  reducer:
  {
    currentUser: currentUserReducer,
    currentArticle: currentArticleReducer
  }
});


export default store;