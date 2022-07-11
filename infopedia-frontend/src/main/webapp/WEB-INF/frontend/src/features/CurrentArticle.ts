import { createSlice, PayloadAction } from "@reduxjs/toolkit";

import Article from "../model/Article";


// This shouldn't store instance of classes.
const dataSlice = createSlice({
  name: "currentArticle",
  initialState: { value: {} },
  reducers:
  {
    setCurrentArticle(state, action: PayloadAction<Article>) {
      state.value = {...action.payload};
    }
  }
});


export const { setCurrentArticle } = dataSlice.actions;
export default dataSlice.reducer;