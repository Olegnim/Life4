package ru.sokolovsoftware.life4.data.repository


import android.content.res.Resources
import android.os.SystemClock
import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular
import ru.sokolovsoftware.life4.domain.repository.DataLife


class RepositoryLife : DataLife {
  private var unicellularList: MutableList<Unicellular> = ArrayList()
  var lifeLoop = 0
  val lifeCycle = 30
  private val sleep = 100
  private var prevTime: Long = SystemClock.uptimeMillis()
  private var canvasHeight: Int = Resources.getSystem().displayMetrics.heightPixels
  private var canvasWidth: Int = Resources.getSystem().displayMetrics.widthPixels

  override fun calculateLife() {
    var stepsQuantity: IntRange = (50..150)
    val currentTime: Long = SystemClock.uptimeMillis()
    if (currentTime <= prevTime + sleep) return else prevTime = SystemClock.uptimeMillis()
    lifeLoop++
    for (unicellular in unicellularList) {
      if (lifeLoop == lifeCycle) {
        if (unicellular.type != TypeUnicellular.LIGHT_GREEN) unicellular.age += 1
      }
      if (unicellular.type == TypeUnicellular.LIGHT_GREEN) continue
      unicellular.cx =
        (unicellular.cx + (1..5).random() + 10 * Math.cos(Math.toRadians(unicellular.directionPath.toDouble()))).toFloat()
      unicellular.cy =
        (unicellular.cy + (1..5).random() + 10 * Math.sin(Math.toRadians(unicellular.directionPath.toDouble()))).toFloat()
      unicellular.steps++
      if (unicellular.steps > stepsQuantity.random()) {
        unicellular.steps = 0
        if (unicellular.directionPath < 260) {
          unicellular.directionPath += (30..90).random()
        } else {
          unicellular.directionPath -= (30..90).random()
        }
      }
      if (unicellular.cx + unicellular.age > canvasWidth) {
        unicellular.cx = canvasWidth - unicellular.age - 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cy + unicellular.age > canvasHeight) {
        unicellular.cy = canvasHeight - unicellular.age - 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cx <= 5 + unicellular.age) {
        unicellular.cx = unicellular.age + 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
      if (unicellular.cy <= 5 + unicellular.age) {
        unicellular.cy = unicellular.age + 10F
        unicellular.directionPath = changePath(unicellular.directionPath)
        unicellular.steps = 0
      }
    }
    if (lifeLoop == lifeCycle) lifeLoop = 0
  }

  private fun changePath(directionPath: Int): Int {
    var result = directionPath
    if (directionPath <= 180) result += (120..170).random() else result -= (120..170).random()
    return result
  }

/*  private fun calculateCenterCircleSmall(sizeCircle: Int, center: Point, angle: Double): Point? {
    //x0,y0 - центр, a - угол, r - радиус.
    val radius = sizeCircle / 2
    val x = (center.x + radius * cos(Math.toRadians(angle))) as Int
    val y = (center.y + radius * Math.sin(Math.toRadians(angle))) as Int
    return Point(x, y)
  }*/

  override fun createUnicellular(unicellular: Unicellular) {
    if (unicellular.cx == 0F) unicellular.cx = (0..canvasWidth).random().toFloat()
    if (unicellular.cy == 0F) unicellular.cy = (0..canvasHeight).random().toFloat()
    this.unicellularList.add(unicellular)
  }

  override fun createListOfUnicellular(unicellularList: MutableList<Unicellular>): MutableList<Unicellular> {
    for (unicellular in unicellularList) {
      createUnicellular(unicellular)
    }
    return unicellularList
  }

  override fun getListOfUnicellular(): MutableList<Unicellular> {
    return unicellularList
  }

  fun setCanvasSize(width: Int, height: Int) {
    canvasWidth = width
    canvasHeight = height
  }
}
