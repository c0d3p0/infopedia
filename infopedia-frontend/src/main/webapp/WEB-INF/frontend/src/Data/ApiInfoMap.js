const port = 3000;
const apiInfoMap = 
{
  getRandomUsers:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/user/data-reduced-random",
    shouldAppendData: true
  },
  getUsersByUsername:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/user/data-reduced-by-username-with",
    shouldAppendData: true
  },
  login:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/user/generate-token",
    shouldAppendData: false
  },
  logout:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/user/expire-token",
    shouldAppendData: false
  },
  createUser:
  {
    method: "POST",
    url:    "http://localhost:" + port +
            "/frontend/user/sign-up",
    shouldAppendData: false
  },
  getUserById:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/user/your-data",
    shouldAppendData: true
  },
  editUser:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/user/your-data",
    shouldAppendData: true
  },
  changeUserPassword:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/user/change-password",
    shouldAppendData: true
  },
  deleteUser:
  {
    method: "DELETE",
    url:    "http://localhost:" + port +
            "/frontend/integration/user/from-user",
    shouldAppendData: true
  },


  getRandomArticles:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/article/random",
    shouldAppendData: true
  },
  getArticlesByTitle:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/article/by-title-with",
    shouldAppendData: true
  },
  getArticlesByUserId:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/article/by-user-id",
    shouldAppendData: true
  },
  getArticleById:
  {
    method: "GET",
    url:    "http://localhost:" + port +
            "/frontend/integration/article",
    shouldAppendData: true
  },
  createArticle:
  {
    method: "POST",
    url:    "http://localhost:" + port +
            "/frontend/integration/article/from-user",
    shouldAppendData: false
  },
  editArticle:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/integration/article/from-user",
    shouldAppendData: true
  },
  deleteArticle:
  {
    method: "DELETE",
    url:    "http://localhost:" + port +
            "/frontend/integration/article/from-user",
    shouldAppendData: true
  },


  createSubContent:
  {
    method: "POST",
    url:    "http://localhost:" + port +
            "/frontend/integration/sub-content/from-user",
    shouldAppendData: false
  },
  editSubContent:
  {
    method: "PATCH",
    url:    "http://localhost:" + port +
            "/frontend/integration/sub-content/from-user",
    shouldAppendData: true
  },
  deleteSubContent:
  {
    method: "DELETE",
    url:    "http://localhost:" + port +
            "/frontend/integration/sub-content/from-user",
    shouldAppendData: true
  }
}


export default apiInfoMap;