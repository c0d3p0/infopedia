const baseURL = "http://localhost:3000/frontend";



const apiMap = new Map<string, IAPIData>([
  [
    "user-find",
    {
      url: baseURL + "/user",
      method: "GET"
    }
  ],
  [
    "user-find-by-id",
    {
      url: baseURL + "/user",
      method: "GET"
    }
  ],
  [
    "user-find-partial-random",
    {
      url: baseURL + "/user/partial-random",
      method: "GET"
    }
  ],
  [
    "user-find-by-username-or-email",
    {
      url: baseURL + "/user/by-username-or-email-with",
      method: "GET"
    }
  ],
  [
    "user-my-data",
    {
      url: baseURL + "/user/your-data",
      method: "GET"
    }
  ],
  [
    "user-check-login",
    {
      url: baseURL + "/user/check-token",
      method: "GET"
    }
  ],
  [
    "user-sign-up",
    {
      url: baseURL + "/user/sign-up",
      method: "POST"
    }
  ],
  [
    "user-add",
    {
      url: baseURL + "/user",
      method: "POST"
    }
  ],
  [
    "user-login",
    {
      url: baseURL + "/user/generate-token",
      method: "PATCH"
    },
  ],
  [
    "user-logout",
    {
      url: baseURL + "/user/expire-token",
      method: "PATCH"
    }
  ],
  [
    "user-edit-my-data",
    {
      url: baseURL + "/user/your-data",
      method: "PATCH"
    }
  ],
  [
    "user-change-my-password",
    {
      url: baseURL + "/user/change-password",
      method: "PATCH"
    }
  ],
  [
    "user-edit",
    {
      url: baseURL + "/user",
      method: "PATCH"
    }
  ],
  [
    "user-remove-my-acount",
    {
      url: baseURL + "/integration/user/remove-account",
      method: "DELETE"
    }
  ],
  [
    "user-remove",
    {
      url: baseURL + "/integration/user",
      method: "DELETE"
    }
  ],


  [
    "article-find-all",
    {
      url: baseURL + "/article",
      method: "GET"
    }
  ],
  [
    "article-find-by-id",
    {
      url: baseURL + "/integration/article",
      method: "GET"
    }
  ],
  [
    "article-find-partial-by-id",
    {
      url: baseURL + "/article",
      method: "GET"
    }
  ],
  [
    "article-find-by-user-id",
    {
      url: baseURL + "/article/by-user-id",
      method: "GET"
    }
  ],
  [
    "article-find-by-title",
    {
      url: baseURL + "/article/by-title-with",
      method: "GET"
    }
  ],
  [
    "article-find-by-content",
    {
      url: baseURL + "/article/by-content-with",
      method: "GET"
    }
  ],
  [
    "article-find-by-sub-content",
    {
      url: baseURL + "/integration/article/by-sub-content-with",
      method: "GET"
    }
  ],
  [
    "article-add",
    {
      url: baseURL + "/integration/article/by-user",
      method: "POST"
    }
  ],
  [
    "article-edit-my-article",
    {
      url: baseURL + "/integration/article/by-user",
      method: "PATCH"
    }
  ],
  [
    "article-edit",
    {
      url: baseURL + "/article",
      method: "PATCH"
    }
  ],
  [
    "article-remove-my-article",
    {
      url: baseURL + "/integration/article/by-user",
      method: "DELETE"
    }
  ],
  [
    "article-remove",
    {
      url: baseURL + "/integration/article",
      method: "DELETE"
    }
  ],


  [
    "sub-content-find-by-id",
    {
      url: baseURL + "/sub-content",
      method: "GET"
    }
  ],
  [
    "sub-content-add",
    {
      url: baseURL + "/integration/sub-content/by-user",
      method: "POST"
    }
  ],
  [
    "sub-content-edit-my-sub-content",
    {
      url: baseURL + "/integration/sub-content/by-user",
      method: "PATCH"
    }
  ],
  [
    "sub-content-edit",
    {
      url: baseURL + "/sub-content",
      method: "PATCH"
    }
  ],
  [
    "sub-content-remove-my-sub-content",
    {
      url: baseURL + "/integration/sub-content/by-user",
      method: "DELETE"
    }
  ],
  [
    "sub-content-remove",
    {
      url: baseURL + "/sub-content",
      method: "DELETE"
    }
  ]
]);


interface IAPIData {
  url: string;
  method: string;
}


export type { IAPIData };
export default apiMap;