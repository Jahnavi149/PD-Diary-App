package com.example.pddiary.models


interface DairyListItem {
    val listItemType: Int
    companion object {
        const val HEADER = 1
        const val ROW = 2
        const val BUTTON = 3
    }
}