package com.example.pddiary.models


data class DairyModel(var time: String, var asleep: Boolean, var on: Boolean,
                      var onWithTroublesome: Boolean, var onWithoutTroublesome: Boolean, var off: Boolean, var med1: Boolean, var med2: Boolean, var measurement: Int,
                      override val listItemType: Int = DairyListItem.ROW
) : DairyListItem
