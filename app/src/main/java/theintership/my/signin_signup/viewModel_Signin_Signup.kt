package theintership.my.signin_signup

import androidx.lifecycle.ViewModel
import theintership.my.model.User

class viewModel_Signin_Signup : ViewModel(){

    var User : User = User(1 , "" , "" , "" , "" , "", 1 , "")

    fun set_user_fullname(fullname : String){
        User.fullname = fullname
    }

    fun set_user_phone(phone : String){
        User.phone = phone
    }

    fun set_user_email(email : String){
        User.email = email
    }

    fun set_user_sex(sex : String){
        User.sex = sex
    }

    fun set_user_age(age : Int){
        User.age = age
    }

    fun set_user_birthday(birthday : String){
        User.birthday = birthday
    }

    fun set_user_pronoun(pronoun : String){
        User.pronoun = pronoun
    }
}