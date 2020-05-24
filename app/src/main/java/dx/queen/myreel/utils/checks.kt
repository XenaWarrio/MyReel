package dx.queen.myreel.utils

/*
* this method for formatting email as a User Id.
* Firebase Database paths must not contain '.', '#', '$', '[', or ']'
* */

fun  String.clearPoints() : String = this.replace(".","")