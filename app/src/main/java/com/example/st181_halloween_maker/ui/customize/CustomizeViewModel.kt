package com.example.st181_halloween_maker.ui.customize

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.extensions.showToast
import com.example.st181_halloween_maker.core.helper.BitmapHelper
import com.example.st181_halloween_maker.core.helper.InternetHelper
import com.example.st181_halloween_maker.core.helper.MediaHelper
import com.example.st181_halloween_maker.core.utils.HandleState
import com.example.st181_halloween_maker.core.utils.SaveState
import com.example.st181_halloween_maker.core.utils.key.CustomLayerKey
import com.example.st181_halloween_maker.core.utils.key.NavigationLayerKey
import com.example.st181_halloween_maker.core.utils.key.ValueKey
import com.example.st181_halloween_maker.data.custom.CustomizeModel
import com.example.st181_halloween_maker.data.custom.ItemColorImageModel
import com.example.st181_halloween_maker.data.custom.ItemColorModel
import com.example.st181_halloween_maker.data.custom.ItemNavCustomModel
import com.example.st181_halloween_maker.data.custom.LayerListModel
import com.example.st181_halloween_maker.data.custom.LayerModel
import com.example.st181_halloween_maker.data.custom.NavigationModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.apply
import kotlin.collections.eachCount
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.groupingBy
import kotlin.collections.indexOfFirst
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.mapIndexed
import kotlin.collections.toCollection
import kotlin.ranges.random
import kotlin.ranges.until
import kotlin.text.set
import kotlin.to

class CustomizeViewModel : ViewModel() {
    // Đếm số lần random, chỉ số được chọn
    var countRandom = 0
    var positionSelected = 0

    // Data từ API hay không
    private val _isDataAPI = MutableStateFlow(false)

    // Trạng thái flip
    private val _isFlip = MutableStateFlow(false)
    val isFlip = _isFlip.asStateFlow()

    //----------------------------------------------------------------------------------------------------------------------
    private val _positionNavSelected = MutableStateFlow(-1)
    val positionNavSelected = _positionNavSelected.asStateFlow()

    private val _positionCustom = MutableStateFlow(-1)
    val positionCustom = _positionCustom.asStateFlow()

    // Data gốc
    private val _dataCustomize = MutableStateFlow<CustomizeModel?>(null)
    val dataCustomize = _dataCustomize.asStateFlow()

    // Danh sách Navigation bottom
    private val _bottomNavigationList = MutableStateFlow(arrayListOf<NavigationModel>())
    val bottomNavigationList = _bottomNavigationList.asStateFlow()

    // Danh sách layer & item
//    private val _categoryList = MutableStateFlow(arrayListOf<ArrayList<LayerModel>>())
//    val categoryList = _categoryList.asStateFlow()

    private val _itemNavList = MutableStateFlow(arrayListOf<ArrayList<ItemNavCustomModel>>())
    val itemNavList = _itemNavList.asStateFlow()

    // Danh sách màu
    private val _colorItemNavList = MutableStateFlow(arrayListOf<ArrayList<ItemColorModel>>())
    val colorItemNavList = _colorItemNavList.asStateFlow()

    // Trạng thái chọn item/màu
    private val _positionColorItemList = MutableStateFlow(arrayListOf<Int>())
    val positionColorItemList = _positionColorItemList.asStateFlow()

    private val _isSelectedItemList = MutableStateFlow(arrayListOf<Boolean>())
    val isSelectedItemList = _isSelectedItemList.asStateFlow()

    private val _isShowColorList = MutableStateFlow(arrayListOf<Boolean>())
    val isShowColorList = _isShowColorList.asStateFlow()

    // Key + Path đã chọn
    private val _keySelectedItemList = MutableStateFlow(arrayListOf<String>())
    val keySelectedItemList = _keySelectedItemList.asStateFlow()

    private val _pathSelectedList = MutableStateFlow(arrayListOf<String>())
    val pathSelectedList = _pathSelectedList.asStateFlow()

    // Danh sách ImageView trên layout
    private val _imageViewList = MutableStateFlow(arrayListOf<ImageView>())
    val imageViewList = _imageViewList.asStateFlow()

    private val _colorListMost = MutableStateFlow(arrayListOf<String>())
    val colorListMost = _colorListMost.asStateFlow()

    //----------------------------------------------------------------------------------------------------------------------
    // Base setter
//    suspend fun setPositionNavSelected(position: Int) {
//        _positionNavSelected.value = position
//    }
//
//    suspend fun setPositionCustom(position: Int) {
//        _positionCustom.value = position
//    }

    fun setDataCustomize(data: CustomizeModel) {
        _dataCustomize.value = data
    }

    fun setIsDataAPI(isAPI: Boolean) {
        _isDataAPI.value = isAPI
    }

    fun setIsFlip() {
        _isFlip.value = !_isFlip.value
    }

    fun setPositionColorItemList(positionList: ArrayList<Int>) {
        _positionColorItemList.value = positionList
    }

    fun setIsSelectedItemList(selectedList: ArrayList<Boolean>) {
        _isSelectedItemList.value = selectedList
    }

    fun setIsShowColorList(showList: ArrayList<Boolean>) {
        _isShowColorList.value = showList
    }

//    suspend fun setCategoryList(categoryList: ArrayList<ArrayList<LayerModel>>) {
//        _categoryList.value = categoryList
//    }

    fun setKeySelectedItemList(keyList: ArrayList<String>) {
        _keySelectedItemList.value = keyList
    }

    fun setPathSelectedList(pathList: ArrayList<String>) {
        _pathSelectedList.value = pathList
    }

    fun setColorListMost(colorList: ArrayList<String>) {
        _colorListMost.value = colorList
    }

    //----------------------------------------------------------------------------------------------------------------------
    // Setter suspend
    suspend fun setPositionColorItem(position: Int, newPosition: Int) {
        _positionColorItemList.value =
            _positionColorItemList.value.mapIndexed { index, oldPosition -> if (index == position) newPosition else oldPosition }
                .toCollection(ArrayList())
    }

    suspend fun setIsSelectedItem(position: Int) {
        _isSelectedItemList.value =
            _isSelectedItemList.value.mapIndexed { index, _ -> index == position }
                .toCollection(ArrayList())
    }

    suspend fun setKeySelected(position: Int, newKey: String) {
        _keySelectedItemList.value = _keySelectedItemList.value
            .mapIndexed { index, oldKey -> if (index == position) newKey else oldKey }
            .toCollection(ArrayList())
    }

    suspend fun setPathSelected(position: Int, newPath: String) {
        _pathSelectedList.value =
            _pathSelectedList.value.mapIndexed { index, oldPath -> if (index == position) newPath else oldPath }
                .toCollection(ArrayList())
    }

    //----------------------------------------------------------------------------------------------------------------------
    // Bottom Navigation
    suspend fun setBottomNavigationList(bottomNavList: ArrayList<NavigationModel>) {
        _bottomNavigationList.value = bottomNavList
    }

    suspend fun setBottomNavigationListDefault() {
        val outputBottomNavigationList = arrayListOf<NavigationModel>()
        _dataCustomize.value!!.layerList.forEach { layerList ->
            outputBottomNavigationList.add(NavigationModel(imageNavigation = layerList.imageNavigation))
        }
        outputBottomNavigationList.first().isSelected = true
        _bottomNavigationList.value = outputBottomNavigationList
    }


//    suspend fun setClickBottomNavigation(position: Int) {
//        _bottomNavigationList.value =
//            _bottomNavigationList.value.map { model -> model.copy(isSelected = model.positionNavigation == position) }
//                .toCollection(ArrayList())
//    }

    //----------------------------------------------------------------------------------------------------------------------
    //  Item Nav / Layer
    suspend fun addValueToItemNavList() {
        val sortedLayerListForBottomNavigation = _dataCustomize.value!!.layerList
        sortedLayerListForBottomNavigation.sortBy { it.positionNavigation }

        sortedLayerListForBottomNavigation.forEachIndexed { index, layer ->
            if (index == 0) {
                _itemNavList.value.add(createListItem(layer, true))
            } else {
                _itemNavList.value.add(createListItem(layer))
            }
        }

    }

    suspend fun setFocusItemNavDefault() {
        for (itemParent in _itemNavList.value) {
            itemParent.forEachIndexed { index, item ->
                item.isSelected = index == 0
            }
        }
        _itemNavList.value.first()[0].isSelected = false
        _itemNavList.value.first()[1].isSelected = true
    }

    suspend fun setItemNavList(positionNavigation: Int, position: Int) {
        _itemNavList.value[positionNavigation] =
            _itemNavList.value[positionNavigation].mapIndexed { index, models -> models.copy(isSelected = index == position) }
                .toCollection(ArrayList())
    }

    suspend fun setClickFillLayer(item: ItemNavCustomModel, position: Int): String {
        val path = item.path
        setKeySelected(positionNavSelected.value, path)
        val pathSelected = if (item.listImageColor.isEmpty()) {
            path
        } else {
            item.listImageColor[positionColorItemList.value[positionNavSelected.value]].path
        }
        setIsSelectedItem(positionNavSelected.value)
        setItemNavList(_positionNavSelected.value, position)
        return pathSelected
    }

    suspend fun setClickRandomLayer(): Pair<String, Boolean> {
        val positionStartLayer = if (positionNavSelected.value == 0) 1 else 2
        val randomLayer = if (positionNavSelected.value == 0) {
            if (itemNavList.value[positionNavSelected.value].size == 1) {
                1
            } else {
                (positionStartLayer..<itemNavList.value[positionNavSelected.value].size).random()
            }
        } else {
            (positionStartLayer..<itemNavList.value[positionNavSelected.value].size).random()
        }

        var randomColor: Int? = null

        var isMoreColors = false

        if (itemNavList.value[positionNavSelected.value][positionStartLayer].listImageColor.isNotEmpty()) {
            isMoreColors = true
            randomColor =
                (0..<(itemNavList.value[positionNavSelected.value][positionStartLayer].listImageColor.size)).random()
        }
        var pathRandom = itemNavList.value[positionNavSelected.value][randomLayer].path
        setKeySelected(positionNavSelected.value, pathRandom)

        if (!isMoreColors) {
            setPositionColorItem(positionCustom.value, 0)
        } else {
            pathRandom = itemNavList.value[positionNavSelected.value][randomLayer].listImageColor[randomColor!!].path
            setPositionColorItem(positionCustom.value, randomColor)
        }
        setPathSelected(positionCustom.value, pathRandom)
        setItemNavList(_positionNavSelected.value, randomLayer)
        if (isMoreColors) {
            setColorItemNav(positionNavSelected.value, randomColor!!)
        }
        return pathRandom to isMoreColors
    }

//    suspend fun setClickRandomFullLayer(): Boolean {
////        countRandom++
////        val isOutTurn = if (countRandom == 5) true else false
//
//        val colorCode =
//            if (colorListMost.value.isNotEmpty()) _colorListMost.value[(0..<colorListMost.value.size).random()] else "#123456"
//        for (i in 0 until _bottomNavigationList.value.size) {
//            val indexNavigationValue = _bottomNavigationList.value[i].positionNavigation
//            val minSize = if (indexNavigationValue == NavigationLayerKey.NAV_BODY_1) 1 else 2
//            if (_itemNavList.value[indexNavigationValue].size <= minSize) {
//                continue
//            }
//            val randomLayer = (minSize..<_itemNavList.value[indexNavigationValue].size).random()
//
//            var randomColor: Int = 0
//
//            val isMoreColors = if (_itemNavList.value[indexNavigationValue][minSize].listImageColor.isNotEmpty()) {
//                randomColor =
//                    _itemNavList.value[indexNavigationValue][randomLayer].listImageColor.indexOfFirst { it.color == colorCode }
//                if (randomColor == -1) {
//                    randomColor = (0..<_itemNavList.value[indexNavigationValue][minSize].listImageColor.size).random()
//                }
//                true
//            } else {
//                false
//            }
//            _keySelectedItemList.value[indexNavigationValue] =
//                _itemNavList.value[indexNavigationValue][randomLayer].path
//
//            val pathItem = if (!isMoreColors) {
//                _positionColorItemList.value[indexNavigationValue] = 0
//                _itemNavList.value[indexNavigationValue][randomLayer].path
//            } else {
//                _positionColorItemList.value[indexNavigationValue] = randomColor
//                _itemNavList.value[indexNavigationValue][randomLayer].listImageColor[randomColor].path
//            }
//            _pathSelectedList.value[setUpPositionCustom(indexNavigationValue)] = pathItem
//            setItemNavList(indexNavigationValue, randomLayer)
//            if (isMoreColors) {
//                setColorItemNav(indexNavigationValue, randomColor)
//            }
//        }
//        return false
//    }
//
//    suspend fun setClickReset(): String {
//        var pathDefault = ""
//        resetDataList()
//        _bottomNavigationList.value.forEachIndexed { index, model ->
//            val isFirst = model.positionNavigation == NavigationLayerKey.NAV_BODY_1
//            val positionSelected = if (isFirst) 1 else 0
//            setItemNavList(model.positionNavigation, positionSelected)
//            setColorItemNav(model.positionNavigation, positionSelected)
//        }
//        pathDefault = _categoryList.value[0][0].image
//        _pathSelectedList.value[CustomLayerKey.CUSTOM_BODY_1] = pathDefault
//        _keySelectedItemList.value[NavigationLayerKey.NAV_BODY_1] = pathDefault
//        _isSelectedItemList.value[NavigationLayerKey.NAV_BODY_1] = true
//        return pathDefault
//    }


    //----------------------------------------------------------------------------------------------------------------------
// Color
//    suspend fun setItemColorDefault() {
//        for (i in 0 until _categoryList.value.size) {
//            // Lấy đối tượng LayerModel đầu tiên trong danh sách con
//            if (_categoryList.value[i].isNotEmpty()) {
//                val currentLayer = _categoryList.value[i][0]
//                var firstIndex = true
//                // Kiểm tra isMoreColors để thêm màu hoặc danh sách rỗng
//                if (currentLayer.isMoreColors) {
//                    val colorList = kotlin.collections.ArrayList<ItemColorModel>()
//                    for (j in 0 until currentLayer.listColor.size) {
//                        val color = currentLayer.listColor[j].color
//                        if (firstIndex) {
//                            colorList.add(ItemColorModel(color, true))
//                        } else {
//                            colorList.add(ItemColorModel(color))
//                        }
//                        firstIndex = false
//                    }
//                    _colorItemNavList.value.add(colorList)
//                } else {
//                    _colorItemNavList.value.add(arrayListOf())
//                }
//            } else {
//                _colorItemNavList.value.add(arrayListOf())
//            }
//        }
//        val getAllColor = kotlin.collections.ArrayList<String>()
//        _itemNavList.value.forEachIndexed { index, nav ->
//            if (index == 0) {
//                if (nav.size > 1) {
//                    val itemNav = nav[1]
//                    itemNav.listImageColor.forEach { colorList ->
//                        getAllColor.add(colorList.color)
//                    }
//                }
//            } else {
//                if (nav.size > 2) {
//                    val itemNav = nav[2]
//                    itemNav.listImageColor.forEach { colorList ->
//                        getAllColor.add(colorList.color)
//                    }
//                }
//            }
//        }
//        setColorListMost(
//            getAllColor
//            .groupingBy { it }
//            .eachCount()
//            .filter { it.value > 3 }.keys
//            .toCollection(ArrayList())
//        )
//    }

    suspend fun setColorItemNav(positionNavSelected: Int, position: Int) {
        _colorItemNavList.value[positionNavSelected] =
            _colorItemNavList.value[positionNavSelected].mapIndexed { index, models -> models.copy(isSelected = index == position) }
                .toCollection(ArrayList())
    }

//    suspend fun setClickChangeColor(position: Int): String {
//        var pathColor = ""
//        _positionColorItemList.value[positionNavSelected.value] = position
//        // Đã chọn hình ảnh chưa
//        if (_keySelectedItemList.value[positionNavSelected.value] != "") {
//            // Duyệt qua từng item trong bộ phận
//            for (item in _categoryList.value[positionNavSelected.value]) {
//                if (item.image == _keySelectedItemList.value[positionNavSelected.value]) {
//                    pathColor = item.listColor[position].path
//                    _pathSelectedList.value[_positionCustom.value] = pathColor
//                }
//            }
//        }
//        setColorItemNav(positionNavSelected.value, position)
//        return pathColor
//    }

//----------------------------------------------------------------------------------------------------------------------
// Extension other

    suspend fun setImageViewList(frameLayout: FrameLayout) {
        _imageViewList.value.addAll(addImageViewToLayout(ValueKey.QUANTITY_LAYER, frameLayout))
    }

    fun addImageViewToLayout(quantityLayer: Int, frameLayout: FrameLayout): ArrayList<ImageView> {
        val imageViewList = kotlin.collections.ArrayList<ImageView>()
        for (i in 0 until quantityLayer) {
            val imageView = ImageView(frameLayout.context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
            frameLayout.addView(imageView)
            imageViewList.add(imageView)
        }
        return imageViewList
    }

    fun createListItem(layers: LayerListModel, isBody: Boolean = false): ArrayList<ItemNavCustomModel> {
        val listItem = arrayListOf<ItemNavCustomModel>()
        val positionCustom = layers.positionCustom
        val positionNavigation = layers.positionNavigation
        if (isBody) {
            listItem.add(
                ItemNavCustomModel(
                    path = layers.layer.first().image,
                    positionCustom = positionCustom,
                    positionNavigation = positionNavigation
                )
            )
        } else {
            listItem.add(
                ItemNavCustomModel(
                    "1.png",
                    positionCustom = positionCustom,
                    positionNavigation = positionNavigation,
                    isSelected = true
                )
            )
            listItem.add(
                ItemNavCustomModel(
                    "1.png",
                    positionCustom = positionCustom,
                    positionNavigation = positionNavigation,
                )
            )
        }
        for (layer in layers.layer) {
            if (!layer.isMoreColors) {
                listItem.add(
                    ItemNavCustomModel(
                        path = layer.image,
                        positionCustom = positionCustom,
                        positionNavigation = positionNavigation
                    )
                )
            } else {
                val listItemColor = ArrayList<ItemColorImageModel>()

                for (colorModel in layer.listColor) {
                    listItemColor.add(
                        ItemColorImageModel(
                            color = colorModel.color,
                            path = colorModel.path
                        )
                    )
                }
                listItem.add(
                    ItemNavCustomModel(
                        path = layer.image,
                        positionCustom = positionCustom,
                        positionNavigation = positionNavigation,
                        isSelected = false,
                        listImageColor = listItemColor,
                    )
                )
            }
        }
        return listItem
    }

    fun saveImageFromView(context: Context, view: View): Flow<SaveState> = flow {
        emit(SaveState.Loading)
        val bitmap = BitmapHelper.createBimapFromView(view)
        MediaHelper.saveBitmapToInternalStorage(context, ValueKey.DOWNLOAD_ALBUM_BACKGROUND, bitmap).collect { state ->
            emit(state)
        }
    }.flowOn(Dispatchers.IO)

    fun checkDataInternet(context: Activity, action: (() -> Unit)) {
        if (!_isDataAPI.value) {
            action.invoke()
            return
        }
        InternetHelper.checkInternet(context) { result ->
            if (result == HandleState.SUCCESS) {
                action.invoke()
            } else {
                context.showToast(R.string.please_check_your_internet)
            }
        }
    }

    suspend fun resetDataList() {
        val positionColorItemList = arrayListOf<Int>()
        val isSelectedItemList = arrayListOf<Boolean>()
        val keySelectedItemList = arrayListOf<String>()
        val isShowColorList = arrayListOf<Boolean>()
        val pathSelectedList = arrayListOf<String>()

        repeat(ValueKey.QUANTITY_LAYER) {
            positionColorItemList.add(0)
            isSelectedItemList.add(false)
            keySelectedItemList.add("")
            isShowColorList.add(true)
            pathSelectedList.add("")
        }

        setPositionColorItemList(positionColorItemList)
        setIsSelectedItemList(isSelectedItemList)
        setKeySelectedItemList(keySelectedItemList)
        setIsShowColorList(isShowColorList)
        setPathSelectedList(pathSelectedList)
    }

    //----------------------------------------------------------------------------------------------------------------------

}