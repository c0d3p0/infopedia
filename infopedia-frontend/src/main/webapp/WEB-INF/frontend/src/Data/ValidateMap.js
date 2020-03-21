const validateMap =
{
  fullName:
  {
    pattern: ".+",
    message: "Full name can't be empty!"
  },
  username:
  {
    pattern: "[a-zA-Z0-9]{6,}",
    message:  "Username must be alphanumerics with " + 
              "at least 6 characters!"
  },
  email:
  {
    pattern: "[^@\\s]+@[^@\\s]+\\.[^@\\s]+",
    message: "Email must be similar to 'text@text.text'!"
  },
  age:
  {
    pattern: "(1[89])|([2-9][0-9])|([1-9][0-9]{2,})",
    message: "Age must be 18 or above!"
  },
  gender:
  {
    pattern: "(male)|(female)",
    message: "Gender must be male or female!"
  },
  country:
  {
    pattern: ".+",
    message: "Country can't be empty!"
  },
  password:
  {
    pattern:  "((?=.*[0-9])(?=.*[a-zA-Z])).{8,}",
    message:  "Password must have at least 1 digit, " +
              "1 letter and 8 characters!"
  },
  confirmPassword:
  {
    pattern: ".+",
    message:  "Confirm password must be the same as the password!",
    equalField: "password"
  },


  loginUser:
  {
    pattern: ".+",
    message: "Username or email can't be empty!"
  },


  currentPassword:
  {
    pattern:  "((?=.*[0-9])(?=.*[a-zA-Z])).{8,}",
    message:  "Current password must have at least 1 digit, " +
              "1 letter and 8 characters!"
  },
  newPassword:
  {
    pattern:  "((?=.*[0-9])(?=.*[a-zA-Z])).{8,}",
    message:  "New password must have at least 1 digit, " +
              "1 letter and 8 characters!"
  },
  confirmNewPassword:
  {
    pattern: ".+",
    message:  "Confirm new password must be the same as the " + 
              "new password!",
    equalField: "newPassword",
  },

  position:
  {
    pattern: "[1-9][0-9]*",
    message: "Position must be a number bigger than 0!"
  },
  type:
  {
    pattern: "(text)|(image-link)|(link)",
    message: "Type must be text, image or link!"
  },
  title:
  {
    pattern: ".+",
    message: "Title can't be empty!"
  },
  content:
  {
    pattern: ".+",
    message: "Content can't be empty!"
  }
}


export default validateMap;