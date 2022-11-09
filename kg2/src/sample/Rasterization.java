package sample;

/**
 * Created by surkov_d_v on 09.11.2022.
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;


public class Rasterization {

    private static int sign(int x) {
        return Integer.compare(x, 0);
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public static void drawLine(
            final GraphicsContext graphicsContext,
            int x0, int y0, int x1, int y1,
            final Color color1, Color color2) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;



        dx = x1 - x0;
        dy = y1 - y0;

        incx = sign(dx);
        /*
         * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
         * справа налево по иксу, то incx будет равен -1.
         * Это будет использоваться в цикле постороения.
         */
        incy = sign(dy);
        /*
         * Аналогично. Если рисуем отрезок снизу вверх -
         * это будет отрицательный сдвиг для y (иначе - положительный).
         */

        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy)
        //определяем наклон отрезка:
        {
            /*
             * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
             * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
             * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
             * по y сдвиг такой отсутствует.
             */
            pdx = incx;	pdy = 0;
            es = dy;	el = dx;
        }
        else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        x = x0;
        y = y0;
        err = el/2;

        int red = (int) color1.getRed();
        int red2 = (int) color2.getRed();

        int r1 = (int) (color1.getRed() + (color1.getRed()-color2.getRed())*(Math.pow((x-x0)*(x-x0)+(y-y0)*(y-y0), 0.5))/(Math.pow(((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)), 0.5)));
        int g1=(int) (color1.getGreen() + (color1.getGreen()-color2.getGreen())*(Math.pow((x-x0)*(x-x0)+(y-y0)*(y-y0), 0.5))/(Math.pow(((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)), 0.5)));
        int b1=(int) (color1.getBlue() + (color1.getBlue()-color2.getBlue())*(Math.pow((x-x0)*(x-x0)+(y-y0)*(y-y0), 0.5))/(Math.pow(((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0)), 0.5)));

        int r2=r1;
        int  g2=g1;
        int  b2=b1;

        pixelWriter.setColor(x, y, color1.rgb(r1, g1, b1));


        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0)
            {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            }
            else
            {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }

            pixelWriter.setColor(x, y, color1.rgb(r2, g2, b2));
        }
    }
}