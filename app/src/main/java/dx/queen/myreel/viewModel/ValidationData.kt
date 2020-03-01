package dx.queen.myreel.viewModel

import dx.queen.myreel.R

class ValidationData {
    companion object{
        fun validateEmail(email:String?) : Int{
            val resultOfValidation =
                commonValidation(email, 12)

            return if (resultOfValidation != R.string.ok){
                resultOfValidation
            }else if(!email!!.contains('@')){
                R.string.should_contains_dog
            }else{
                R.string.ok
            }
        }
        fun validatePassword(password:String?): Int{
           val resultOfPasswordValidation =
               commonValidation(password, 10)
            val regex ="^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$"

            return if (resultOfPasswordValidation != R.string.ok) {
                resultOfPasswordValidation
            }else if (regex.matches(password!!.toRegex())) {
                R.string.password_should_contains
            }else{
                R.string.ok
            }


        }

        fun validateUserName(username:String?): Int{
            val resultUserNameValidation =
                commonValidation(username, 3)

            return if(resultUserNameValidation!= R.string.ok){
                resultUserNameValidation
            }else{
                R.string.ok
            }

        }

//        fun validateDate(date:String?): Int{
//
//        }

       private fun commonValidation(obj: String?, symbolCount : Int):Int{
            when{
                obj.isNullOrEmpty() -> return R.string.cant_be_empty
                obj.length < symbolCount -> return R.string.should_contains_more_symbols
            }
            return R.string.ok
        }
    }


}