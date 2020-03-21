const pageActionMap = 
{
	listRandomArticles:
	{
		key: "listRandomArticles",
		onLoadApiInfo: "getRandomArticles",
		itemsPageAction: "showArticle",
		components: "listArticles",
		pageContent: "welcome"
	},
	showArticle:
	{
		key: "showArticle",
		onLoadApiInfo: "getArticleById",
		components: "showArticle",
		pageContent: "showArticle"
	},
	listArticlesByTitle:
	{
		key: "listArticlesByTitle",
		onLoadApiInfo: "getArticlesByTitle",
		itemsPageAction: "showArticle",
		components: "listArticles",
		pageContent: "listArticlesByTitle"
	},
	listArticlesByUserId:
	{
		key: "listArticlesByUserId",
		onLoadApiInfo: "getArticlesByUserId",
		itemsPageAction: "showArticle",
		components: "listArticles",
		pageContent: "listArticlesByUserId"
	},

	listUsersByUsername:
	{
		key: "listUsersByUsername",
		onLoadApiInfo: "getUsersByUsername",
		itemsPageAction: "listArticlesByUserId",
		components: "listUsers",
		pageContent: "listUsersByUsername"
	},
	createUser:
	{
		key: "createUser",
		itemApiInfo: "createArticle",
		onLoadApiInfo: "createUser"
	},
	login:
	{
		key: "login",
		onLoadApiInfo: "login"
	},
	logout:
	{
		key: "logout",
		onLoadApiInfo: "logout"
	},
	showUserForm:
	{
		key: "showUserForm",
		onLoadApiInfo: "getUserById",
		components: "editUser"
	},
	showDeleteUserForm:
	{
		key: "showDeleteUserForm",
		components: "deleteUser"
	},
	editUser:
	{
		key: "editUser",
		actionApiInfo: "editUser"
	},
	changeUserPassword:
	{
		key: "changeUserPassword",
		actionApiInfo: "changeUserPassword"
	},
	deleteUser:
	{
		key: "deleteUser",
		actionApiInfo: "deleteUser"
	},

	listToEditUserArticles:
	{
		key: "listToEditUserArticles",
		onLoadApiInfo: "getArticlesByUserId",
		itemsPageAction: "editArticle",
		components: "editArticles",
		pageContent: "editArticles"
	},
	listToDeleteUserArticles:
	{
		key: "listToDeleteUserArticles",
		onLoadApiInfo: "getArticlesByUserId",
		itemsPageAction: "deleteArticle",
		components: "editArticles",
		pageContent: "deleteArticles"
	},
	newArticle:
	{
		key: "newArticle",
		components: "editArticle",
		itemApiInfo: "createArticle",
		postApiInfoPageAction: "editArticle",
		pageContent: "newArticle"
	},
	editArticle:
	{
		key: "editArticle",
		components: "editArticle",
		onLoadApiInfo: "getArticleById",
		itemApiInfo: "editArticle",
		postApiInfoPageAction: "editArticle",
		pageContent: "editArticle",
	},
	deleteArticle:
	{
		key: "deleteArticle",
		components: "editArticle",
		onLoadApiInfo: "getArticleById",
		itemApiInfo: "deleteArticle",
		postApiInfoPageAction: "listToDeleteUserArticles",
		pageContent: "deleteArticle"
	},


	newArticleSubContent:
	{
		key: "newArticleSubContent",
		actionApiInfo: "newArticleSubContent"
	},
	editArticleSubContent:
	{
		key: "editArticleSubContent",
		actionApiInfo: "editArticleSubContent"
	},
	deleteArticleSubContent:
	{
		key: "deleteArticleSubContent",
		actionApiInfo: "deleteArticleSubContent"
	},
}



export default pageActionMap;