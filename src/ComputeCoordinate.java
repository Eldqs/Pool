/**
 * Created by Eldos on 21.06.2015.
 */

public class ComputeCoordinate {
    static int accuracy = 5;
    public static void main(String[] args) {
        double x = 9999;
        double y = 9999;
        double r = 1;
        Circle[] circles = filling(999, r);
        Circle pool = compMax(circles, circles.length, x, y);
        System.out.println(pool);
    }

    // Заполнение коллон
    public static Circle[] filling(int count, double radius){
        Circle[] circles = new Circle[count];
        double x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            x += radius + Math.random();
            y += radius + Math.random();
            circles[i] = new Circle(x, y, radius);
        }
        return circles;
    }

    //Проверка пересечения колонны с другими колоннами и стенами комнаты
    static boolean isIntersection(Circle circle, Circle[] circles, int cCount, double x, double y) {
        //Проверка что бассейн внутри комнтаы
        if(!((circle.x - circle.r > 0) && (circle.y - circle.r > 0) && (circle.x + circle.r < 0 + x) && (circle.y + circle.r < 0 + y)))
            return false;
        for (int i = 0; i < cCount; i++)
            //Проверка столкновения двух коллон
            if (Math.sqrt((circle.x - circles[i].x) * (circle.x - circles[i].x) + (circle.y - circles[i].y) * (circle.y - circles[i].y)) < (circle.r + circles[i].r))
                return false;
        return true;
    }

    // Вычисление максимального радиуса для Бассейна с точностью dt
    static Circle compMaxRadius(Circle t, Circle[] circles, int cCount, double x, double y, double dt) {
        Circle max = new Circle(t.x, t.y, t.r);
        while (true) {
            t.r += dt;
            if (isIntersection(t, circles, cCount, x, y)) {
                if (t.r > max.r)
                    max =  new Circle(t.x, t.y, t.r);
            } else break;
        }
        return max;
    }

    // Вычисление максимального радиуса для бассейна который не пересекался бы с коллонами и стенами комнаты
    static Circle compMax(Circle[] circles, int cCount, double x, double y) {
        Circle max = new Circle(0, 0, 0), tmp;
        double i, j;
        double dt = 0, prev_dt;
        for (int d = 0; d <= accuracy; d++) {
            prev_dt = dt;
            dt = 1 / Math.pow((10), (d));
            tmp = new Circle(max.x, max.y, max.r);
            if (prev_dt == 0) {
                for (i = dt; i < x; i += dt) {
                    for (j = dt; j < y; j += dt) {
                        tmp.x = i;
                        tmp.y = j;
                        tmp = compMaxRadius(tmp, circles, cCount, x, y, dt);
                        if (tmp.r > max.r)
                            max = new Circle(tmp.x, tmp.y, tmp.r);
                    }
                }
            } else {
                for (i = max.x - prev_dt; i < max.x + prev_dt; i += dt) {
                    for (j = max.y - prev_dt; j < max.y + prev_dt; j += dt) {
                        tmp.x = i;
                        tmp.y = j;
                        tmp = compMaxRadius(tmp, circles, cCount, x, y, dt);
                        if (tmp.r > max.r)
                            max = new Circle(tmp.x, tmp.y, tmp.r);
                    }
                }
            }
        }
        return max;
    }


    public static class Circle {
        public double x, y, r;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
        // Округление координат
        public void  rounding() {
            x = division(x);
            y = division(y);
            r = division(r);
        }

        public double division(double d){
            int tmp =  (int) (d * 100000);
            return (double) tmp/ 100000;
        }

        @Override
        public String toString() {
            this.rounding();
            return "Pool x = " + x + ", y = " + y + ", max radius = " + r;

        }
    }
}
