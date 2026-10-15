package utils

object SlicedRowPerPageHelper {

  def returnSlicedRowsPerPage[A](
    list: List[A],
    startIndex: Int,
    count: Int
  ): List[A] = {
    val remainingList = list.slice(startIndex, list.length)
    if (remainingList.length > count) {
      val paginationIndex = (startIndex / count) + 1
      val endIndex        = paginationIndex * count
      list.slice(startIndex, endIndex)
    } else {
      remainingList
    }

  }

}
