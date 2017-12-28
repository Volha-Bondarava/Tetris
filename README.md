# Tetris
BSUIR, 550502
***

# **Требования к проекту**
## **1 Введение**
Название программы – Tetris. 
Данный продукт представляет собой компьютерную мини-игру в своём классическом варианте.

**Правила игры:**

У игрока есть прямоугольное поле шириной 10 и высотой 25 клеток. Сверху вниз падают фигурки, которые можно поворачивать на  90° а также двигать по горизонтали. 
Также можно "сбрасывать" фигурку (т.е. ускорять её падение), когда игрок уже решил, куда фигурка должна упасть. Фигурка летит до тех пор, пока не упрётся в
другую фигурку либо же не попадёт на дно стакана. Если при этом заполнился гозизонтальный ряд из 10 клеток, то этот ряд пропадает, а игрок получает определённое 
количество очков. Все ряды выше пропавшего после опускаются на одну клетку. Игра заканчивается, когда новая фигурка не может поместиться в стакан. 
Таким образом, задачей игрока является заполнять ряды, не заполняя при этом сам стакан (по вертикали) как можно дольше, чтобы получить больше очков. 
Игра не имеет временных ограничений, всё зависит только от вертикальной заполнености поля и наличия места для новых фигурок. 

Начисление очков за заполненные ряды происходит следующим образом: 

1 линия — 100 очков,  
2 линии — 300 очков, 
3 линии — 700 очков, 
4 линии (то есть, сделать Тетрис) — 1500 очков.

## **2 Требования пользователя**
### **2.1 Программные интерфейсы**
Приложение написано на языке Java с использованием интегрированной среды разработки (IDE) IntellijIdea.
Для создания графического интерфейса использовалась билиотека Swing.

### **2.2 Интерфейс пользователя**
Игра имеет следующий внешний вид:

![alt-текст](https://github.com/OlgaBondareva/Tetris/blob/master/Documentation/Images/tetris.jpg "Внешний вид игры")

![alt-текст](https://github.com/OlgaBondareva/Tetris/blob/master/Documentation/Images/tetris1.jpg "Внешний вид игры")

![alt-текст](https://github.com/OlgaBondareva/Tetris/blob/master/Documentation/Images/gameOver.jpg "Game Over")

### **2.3 Характеристики пользователей**
Целевая аудитория приложения - люди любого возраста.

## **3 Системные требования**
Для запуска игры необходим любой современный персональный компьютер. 

### **3.1 Нефункциональные требования**
###### **3.1.1 АТРИБУТЫ КАЧЕСТВА**
1. **_Доступность_** – приложение может работать неограниченное количество времени, т.е. 24x7.
