# Faction v0.0.3
# Plugin for Spigot/Paper Server v1.18.1 on Java 16
# Developed in Eclipse Maven Project 

Objetivos:

• Facciones
✓ 1. Inicialmente con un máximo de 3 usuarios por facción, se podrá aumentar la cantidad de miembros según el flujo de usuarios en el servidor.
✓ 2. No permite el PVP entre miembros de la misma facción.
  3. Se permite el envió de SA Cash entre facciones.
  4. Entrar en Modo Traslado activa el PVP de toda la facción.
  5. El Nivel de Poder de la facción es la suma de Nivel de Poder entre los integrantes de la facción.
  6. Se puede seleccionar un mob o grupo de mobs hostiles para que aparezcan en caso de ser eliminada en batalla toda la facción, los mobs contaran con el bonus de daño por Nivel de Poder y podrán ser seleccionador a partir del Nivel del Poder de la facción


• Tratados de Paz
✓ 1. Si eres asesinado por un jugador entras en modo tratado de paz.
✓ 2. Otros usuarios no pueden hacerte daño a menos de que tú les ataques primero durante un tratado de paz.
✓ 3. Los tratados de paz tienen una duración proporcional al Nivel de Poder, en donde a mayor nivel menor duración.
✓ 4. Entrar en este modo te entrega una brújula (no traspasable) que te lleva a un radio de 50 bloques del usuario que te asesino.
✓ 5. Cobrar Venganza durante el tratado de paz no otorga la brújula a ninguno de los jugadores/facciones de la batalla.


• Nivel de Poder
✓ 1. El Nivel de Poder se calcula a partir del poder de arcos, espadas, materiales de armaduras, manzanas doradas, Enderchest y pociones que se tengan en el inventario, a partir del set con la suma más alta de puntos a la vez.
✓ 2. Este nivel define que jugadores puedes pelear entre sí, para llevar un PVP de forma más equilibrada.
  3. Este nivel permite que usuarios de un nivel inferior puedan iniciar una pelea contra usuarios de niveles superiores, mas no de forma inversa.
  4. Ganar batallas entre usuarios de niveles de poder superior permite ganar más SA Cash.
  5. Los primeros niveles de poder no otorgan SA Cash, para evitar el Smurf Farm.
  6. El aldeano Errante ofrece mejores artículos a mayor Nivel de Poder que posea el jugador.
✓ 7. El daño ocasionado por los mobs aumenta proporcional al Nivel de Poder.
  8. La suma de nivel de poder por facción permite proteger áreas mas grandes, por lo que las áreas de protecciones de zonas son dinámicas.


• Aldeano Errante
  1. El aldeano errante es una versión modificada al aldeano errante, este se aparece después de 2 horas continuas en el juego.
  2. Ofrece ítems para el PVP con encantamientos especiales no Vanilla (Robo de Vida, Furia de Thor, Whiter Effect, Blocks, Velocidad de Movimiento, Magma Summon, Notch Apples, KGB), estos ítems no pueden ser dropeados ni tomados por usuarios de tu misma facción desde cofres.
  3. Asesinar al aldeano errante provoca que su próxima reaparición sea tras 4 horas continuas para toda la facción, e invoca una horda de Vindicadores.


• SA Cash
  1. SA Cash, es la moneda del juego, se obtiene tras asesinar a otro jugador.
  2. Compra tratados de paz inferiores a los tratados de paz por muerte, no funcional durante el modo Venganza.
  3. Intercambiable por esmeraldas
  4. Activa Modo traslado; se puede trasladar SA Cash aunque este no sea físico, cuando se este a 2K de bloques del centro del mapa y hayan al menos 2 facciones en línea y al menos un usuario de una facción enemiga del mismo rango de Nivel de Poder, esto permite multiplicar x1.20 el SA Cash una vez se llegue al Banco del centro del mapa, de morir en el modo traslado por otra facción se divide entre ellos todo el SA Cash que se ofreció para el traslado, cualquier Nivel de Poder puede hacerte entrar en modo PVP mientras se tenga activo. No se permite iniciar el Modo Traslado si se tienen Elytras.
  5. Permite adquirir un cofre virtual de 1x9, se desactiva al morir y el contenido del cofre virtual se traslada al inventario del jugador al reaparecer.


• Protecciones
  1. Las protecciones por facción permiten evitar la destrucción de construcciones
  2. Las protecciones de construcción no bloquean el acceso a tus Cofres, Barriles ni Shulkers
  3. El área de las protecciones depende de la suma de Nivel de Poder de la facción, con un máximo de un radio de 200 bloques
  4. No evitan el PVP
  5. Si un enemigo entra a la protección de tu facción esta avisara a todos los miembros de que un invasor entro a menos que el enemigo tenga una poción de invisibilidad o un objeto especial que lo evite.
  6. Durante los primeros rangos del Nivel de Poder existirá un radio de 2 bloque del centro de la protección con protección de cofres, una vez superado estos rangos de poder desaparecerá estas protecciones y no se volverán a activar, aunque se vuelvan a los primeros rangos de Nivel de Poder.

• Sesiones
✓ 1. El usuario requiere registrarse con una contraseña
✓ 2. El usuario requiere iniciar sesión con una contraseña
✓ 3. El usuario puede cambiar su contraseña
✓ 4. El usuario no requiere iniciar sesión con una contraseña si usa la misma Direccion MAC. 
✓ 5. Sesión segura, durante el inicio de sesión el jugador no recibe daño ni efectos, ni puede modificar su entorno o inventario.

• Penalizaciones
  1. Morir seis veces en un día evitara el acceso al servidor hasta el siguiente día.
  2. Uso de hacks de cualquier tipo es ban directo a ip y de la facción junto con limpieza de cofres e inventarios que se hayan dejado en el mundo.
  3. El farmeo de SA Cash sin PVP “real” entre facciones reinicia el SA Cash de ambas facciones e inventarios virtuales.
  
  
