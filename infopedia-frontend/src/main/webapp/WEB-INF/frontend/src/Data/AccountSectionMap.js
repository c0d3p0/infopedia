const accountSectionMap = 
{
	yourData:
	{
		key: "yourData",
		title: "Your Data",
		pageAction: "showUserForm",
		dropdown: true,
		sidebox: true
	},
	newArticle:
	{
		key: "newArticle",
		title: "New Article",
		pageAction: "newArticle",
		dropdown: true,
		sidebox: true
	},
	editArticles:
	{
		key: "editArticles",
		title: "Edit Articles",
		pageAction: "listToEditUserArticles",
		dropdown: true,
		sidebox: true
	},
	deleteArticles:
	{
		key: "deleteArticles",
		title: "Delete Articles",
		pageAction: "listToDeleteUserArticles",
		dropdown: false,
		sidebox: true
	},
	deleteAccount:
	{
		key: "deleteAccount",
		title: "Delete Account",
		pageAction: "showDeleteUserForm",
		dropdown: false,
		sidebox: true
	},
	logout:
	{
		key: "logout",
		title: "Log Out",
		pageAction: "logout",
		dropdown: true,
		sidebox: false
	}
}


export default accountSectionMap;