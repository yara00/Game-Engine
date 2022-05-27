class State (rows: Int, cols: Int) {
  var flag: Boolean = true
  var state = Array.ofDim[String](rows, cols)
  def printState(): Unit = {
    println()
    println("A B C D E F G H")
    println()
    for(i <- 0 until state.length) {
      for(j <- 0 until state.length) {
        print(state(i)(j) + " ")
      }
      println()
    }
  }
}
