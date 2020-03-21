import ListArea from "../Component/ListArea/ListArea";
import ArticleArea from "../Component/ArticleArea/ArticleArea";
import EditArea from "../Component/EditArea/EditArea";
import EditCenterbox from "../Component/EditCenterbox/EditCenterbox";
import UserForm from "../Component/UserForm/UserForm";
import DeleteUserForm from "../Component/DeleteUserForm/DeleteUserForm";
import ArticleForm from "../Component/ArticleForm/ArticleForm";
import ListArticleCenterbox from
		"../Component/ListArticleCenterbox/ListArticleCenterbox";
import ListUserCenterbox from
		"../Component/ListUserCenterbox/ListUserCenterbox";

const screenComponentsMap = 
{
	showArticle:
	{
		main: ArticleArea
	},
	listArticles:
	{
		main: ListArea,
		centerbox: ListArticleCenterbox
	},
	listUsers:
	{
		main: ListArea,
		centerbox: ListUserCenterbox
	},
	editUser:
	{
		main: EditArea,
		centerbox: EditCenterbox,
		centerboxChild: UserForm
	},
	deleteUser:
	{
		main: EditArea,
		centerbox: EditCenterbox,
		centerboxChild: DeleteUserForm
	},
	
	editArticles:
	{
		main: EditArea,
		centerbox: ListArticleCenterbox,
	},
	deleteArticles:
	{
		main: EditArea,
		centerbox: ListArticleCenterbox
	},

	newArticle:
	{
		main: EditArea,
		centerbox: EditCenterbox,
		centerboxChild: ArticleForm
	},
	editArticle:
	{
		main: EditArea,
		centerbox: EditCenterbox,
		centerboxChild: ArticleForm
	},
	deleteArticle:
	{
		main: EditArea,
		centerbox: EditCenterbox,
		centerboxChild: ArticleForm
	}
}


export default screenComponentsMap;