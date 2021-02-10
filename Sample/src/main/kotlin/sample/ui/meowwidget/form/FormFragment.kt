/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.ui.meowwidget.form

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import meow.ktx.instanceViewModel
import meow.ktx.toastL
import sample.R
import sample.databinding.FragmentFormBinding
import sample.ui.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Material Form Fragment.
 *
 * @author  Ali Modares
 * @version 1.0.0
 * @since   2020-03-22
 */

class FormFragment : BaseFragment<FragmentFormBinding>() {

    private val viewModel: FormViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_form

    @SuppressLint("PrivateResource", "SimpleDateFormat")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.bt.setOnClickListener {
            binding.fv.validate {
                Log.d("testText", "Text is : ${binding.et.text.toString()}")
                toastL(binding.et.text.toString())
            }
        }

        binding.et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.et.text!!.length > binding.et.counterMaxLength)
                    binding.et.error = getString(R.string.limit_error)
                else
                    binding.et.isErrorEnabled = false
            }
        })

        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "آیتم 1")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            items
        )
//        binding.sp1.setAdapter(adapter)


        binding.sp1
            .addItem(R.string.item1, R.string.description1, R.drawable.ic_error)
            .addItem(R.string.item2, R.string.description2, R.drawable.ic_error)
            .addItem(R.string.item3, imageViewResId = R.drawable.ic_error)
            .addItem(R.string.item4, imageViewResId = R.drawable.ic_error)
            .addItem(R.string.item5, R.string.description3)
            .addItem(R.string.item6, R.string.description4)
            .addItem(R.string.item7)
            .addItem(R.string.item8)
            .build()

        binding.sp1.setOnItemCLickListener { parent, view, position, id ->
            toastL(id.toString())
        }

        binding.pv.error = getString(R.string.error_sample)

        binding.hintButton.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText("Please Choose your date")
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener { selection ->

                val simpleFormat = SimpleDateFormat("MM/dd/yyyy")
                binding.hintButton.titleText = simpleFormat.format(Date(selection))

//                Date and Time Pattern                Result
//                -----------------------------        ---------------------------------
//                "yyyy.MM.dd G 'at' HH:mm:ss z"       2001.07.04 AD at 12:08:56 PDT
//                "EEE, MMM d, ''yy"                   Wed, Jul 4, '01
//                "h:mm a"                             12:08 PM
//                "hh 'o''clock' a, zzzz"              12 o'clock PM, Pacific Daylight Time
//                "K:mm a, z"                          0:08 PM, PDT
//                "yyyyy.MMMMM.dd GGG hh:mm aaa"       02001.July.04 AD 12:08 PM
//                "EEE, d MMM yyyy HH:mm:ss Z"         Wed, 4 Jul 2001 12:08:56 -0700
//                "yyMMddHHmmssZ"                      010704120856-0700
//                "yyyy-MM-dd'T'HH:mm:ss.SSSZ"         2001-07-04T12:08:56.235-0700
//                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"       2001-07-04T12:08:56.235-07:00
//                "YYYY-'W'ww-u"                       2001-W27-3
            }
            picker.show(parentFragmentManager, "date_picker_tag")
        }
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}