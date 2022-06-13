# FrontierMap
![icon](https://user-images.githubusercontent.com/79571688/172386545-374b179f-e898-4738-a109-c17b1eb6bb42.png)

Отображение областей(полигонов) на карте OpenStreetMap по geojson приходящего от сервера.
На время загрузки данных от сервера - показывается Lottie анимация. При клике - область окрашивается, и показывает длину периметра.
Также выполнена обработка ошибок при выполнении api запроса(отсутствие интернет, ошибка сервера и т.д.): навигация на специальный фрагмент.

Проект выполнен по архитектуре MVVM. Сделаны Unit(mockito) тесты для вью-модели, юз-кейса, сервиса и апи.

Стек: kotlin, koin, retrofit+okhttp, mockito, osmdroid+osmbonuspack, coroutine.


![result-2](https://user-images.githubusercontent.com/79571688/172390917-78bf2f6c-96b5-4997-ab39-57833aa7f1a2.gif)

Сделано объединение полигонов по 180 меридиану (если geojson приходит с таким разделением):

![mergePoly](https://user-images.githubusercontent.com/79571688/173332157-38c9505f-7b6f-43b5-be10-e1edcff8c43b.png)
