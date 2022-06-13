package ru.sokolovsoftware.life4.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView
import ru.sokolovsoftware.life4.data.repository.RepositoryLife
import ru.sokolovsoftware.life4.domain.models.TypeUnicellular
import ru.sokolovsoftware.life4.domain.models.Unicellular


internal class DrawView(context: Context?, listOfUnicellular: MutableList<Unicellular>) :
  SurfaceView(context), SurfaceHolder.Callback {
  private var drawThread: DrawThread? = null
  private val unicellularData = RepositoryLife()
  var listOfUnicellular: MutableList<Unicellular> =
    unicellularData.createListOfUnicellular(listOfUnicellular)

  override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
  }

  override fun surfaceCreated(holder: SurfaceHolder) {
    drawThread = DrawThread(getHolder())
    drawThread!!.setRunning(true)
    drawThread!!.start()
  }

  override fun surfaceDestroyed(holder: SurfaceHolder) {
    var retry = true
    drawThread!!.setRunning(false)
    while (retry) {
      try {
        drawThread!!.join()
        retry = false
      } catch (e: InterruptedException) {
      }
    }
  }

  internal inner class DrawThread(private val surfaceHolder: SurfaceHolder) : Thread() {
    private var running = true

    fun setRunning(running: Boolean) {
      this.running = running
    }

    override fun run() {
      var canvas: Canvas?
      while (running) {
        canvas = null
        try {
          canvas = surfaceHolder.lockCanvas(null)
          if (canvas == null) continue
          unicellularData.setCanvasSize(canvas.width, canvas.height)
          canvas.drawColor(Color.WHITE)
          unicellularData.calculateLife()
          listOfUnicellular = unicellularData.getListOfUnicellular()
          for (unicellular in listOfUnicellular) {
            if (!unicellular.live) continue
            val paint = Paint()

            if (unicellular.type == TypeUnicellular.RED) paint.color = Color.RED
            if (unicellular.type == TypeUnicellular.GREEN) paint.color = Color.GREEN
            if (unicellular.type == TypeUnicellular.LIGHT_GREEN) {
              paint.color = Color.LTGRAY
              paint.style = Paint.Style.STROKE
              paint.strokeWidth = 2F
            } else {
              paint.style = Paint.Style.FILL
            }
            if (unicellular.type == TypeUnicellular.BLUE) paint.color = Color.CYAN
            canvas.drawCircle(unicellular.cx, unicellular.cy, unicellular.size.toFloat(), paint)
          }
          val paint = Paint()
          paint.color = Color.BLACK
          paint.textAlign = Paint.Align.LEFT
          paint.textSize = 50F
          val k: Int = unicellularData.getListOfUnicellular().size
          canvas.drawText("Quantity unicellular: $k pcs", 100F, 100F, paint)
          val paint2 = Paint()
          paint2.color = Color.BLACK
          paint2.textAlign = Paint.Align.LEFT
          paint2.textSize = 50F
          val x: Long = unicellularData.globalCycle
          canvas.drawText("Global Cycle: $x", 100F, 150F, paint2)
        } finally {
          if (canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas)
          }
        }
      }
    }
  }

  init {
    holder.addCallback(this)
  }
}

