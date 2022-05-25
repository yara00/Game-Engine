class State (rows: Int, cols: Int) {
  var flag: Boolean = true
  var state = Array.ofDim[String](rows, cols)

  def printState(): Unit = {
    for(i <- 1 until state.length) {
      for(j <- 1 until state.length) {
        print(state(i)(j) + " ")
      }
      println()
    }
  }
}
