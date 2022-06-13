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
            paint.style = Paint.Style.FILL
            if (unicellular.type == TypeUnicellular.RED) paint.color = Color.RED
            if (unicellular.type == TypeUnicellular.GREEN) paint.color = Color.GREEN
            if (unicellular.type == TypeUnicellular.LIGHT_GREEN) paint.color = Color.GRAY
            if (unicellular.type == TypeUnicellular.BLUE) paint.color = Color.BLUE
            canvas.drawCircle(unicellular.cx, unicellular.cy, unicellular.size.toFloat(), paint)
          }
          val paint = Paint()
          paint.color = Color.BLACK
          paint.textAlign = Paint.Align.RIGHT
          paint.textSize = 120F;
          canvas.drawText(unicellularData.getListOfUnicellular().size.toString(), 200F, 200F, paint)
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

