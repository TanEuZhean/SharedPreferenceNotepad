package com.example.noteclone.data

import android.util.Log
import java.lang.Integer.parseInt

data class Note(
    var id: Int,
    var title: String,
    var content: String,
    var time: String,
    var color: String
) {
    override fun toString() : String {
        var res = ""

        res = res.plus("$id,$title,$content,$time,$color")

        return res
    }
}


fun toNote(string: String) : Note {
    var split = string.split(",")

    var note : Note = Note(
        id = parseInt(split[0]),
        title = split[1],
        content = split[2],
        time = split[3],
        color = split[4]
    )

    return note
}
