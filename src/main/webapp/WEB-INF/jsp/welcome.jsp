<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <div class="row">
	    <h1>Bienvenido a Idus Martii</h1>
	    Idus Martii es una aplicación desarrollada por 5 estudiantes de ingeniería del software de la Universidad de Sevilla
	    en la asignatura "desarrollo y pruebas 1". Ha sido desarrollada por los siguientes estudiantes: 
	    
	    <ul>
		    <p>-Álvaro Úbeda Ruiz</p>
		    <p>-Mario Pérez Coronel</p>
		    <p>-Juan Carlos Gómez Ramirez</p>
		    <p>-Carlos Garrido Rodriguez</p>
		    <p>-Ángel Lorenzo Casas</p>
	    </ul>
	    <h2>Como jugar y normas de juego</h2>
	    <h3>Preparación</h3>
	    <p>
	    1. Se separan tantas parejas de cartas de facción “Leal”
		y “Traidor” como jugadores haya menos 1. Se añaden a
		éstas 2 cartas de facción “Mercader” y se baraja el mazo
		formado.
		</p>
		<p>
		2. Cada jugador recibe 2 cartas de facción del mazo,
		boca abajo. Las puede examinar pero no mostrar al
		resto de jugadores.
		</p>
		<p>
		3. Se separan las 6 cartas de voto y las 4 cartas de rol y
		se dejan en el centro de la mesa, boca arriba.
	   	</p>
   	</div>
   	
   	<div class="row">
		   	<h3>Cómo se juega la partida</h3>
		   	<p>
		   	La partida se desarrolla en dos rondas. Cada ronda tiene tantos turnos como jugadores haya en mesa. Una vez finalizada
			la primera ronda, empieza la segunda; una vez finalizada la segunda (cuando el jugador inicial reciba por 3a vez la carta de
			Cónsul), se procede al final de la partida.
		   	</p>
		   	<p>
		   	-Durante <b>LA PRIMERA RONDA</b>, los turnos se componen de los pasos siguientes:
		   	</p>
		   	<ul>
			   	<p>
			   	<b>1. VOTACIÓN:</b> Cada Edil coge una carta de voto verde y una
				roja. Luego seleccionan su voto secretamente y colocan dicha
				carta frente a si, boca abajo.
			   	</p>
			   	<p>
			   	<b>2. VETO:</b> El Pretor escoge UNA de dichas cartas de voto y
				la mira. Si lo desea, puede forzar a dicho Edil a intercambiar
				dicha carta de voto con la carta que le queda en la mano.
			   	</p>
			   	<p>
			   	<b>3. CONTEO:</b> El Cónsul recoge los votos finales, los mezcla
				bajo la mesa, y los muestra al resto de jugadores. El Cónsul
				contabiliza los votos en la carta de “Svffragivm”: por cada
				voto, avanza el marcador de dicha facción un espacio. Si
				un marcador de facción sobrepasa el espacio de límite de
				jugadores, proceded al FINAL DE LA PARTIDA.
			   	</p>
			   	<p>
			   	<b>4. FIN DE TURNO:</b> El Cónsul descarta, boca abajo, UNA de
				sus cartas de facción a su elección - de esta forma escogen su
				bando, lo que será crucial para el FINAL DE LA PARTIDA.
				El jugador inicial ignora este paso en su primer turno.
				Cada jugador con carta de rol entrega dicha carta al jugador
				a su izquierda (el Pretor pasará a ser Cónsul, el primer Edil
				pasará a ser Pretor, etc) y empieza un nuevo turno.
			   	</p>
		   	</ul>
		   	<p>
		   	-Durante <b>LA SEGUNDA RONDA</b>, se siguen los mismos pasos
			en cada turno, con los siguientes añadidos:
		   	</p>
		   	<ul>
			   	<p>
			   	<b>1. VOTACIÓN:</b> El Cónsul asigna el resto de cartas de rol,
				cada una a un jugador diferente. Los jugadores no pueden
				repetir rol dos turnos seguidos (a excepción de partidas a 5
				jugadores donde se podrá repetir un Edil). Adicionalmente,
				cada Edil coge una carta de voto amarilla (además de una
				carta roja y una verde) y puede votar con cualquiera de ellas
			   	</p>
			   	<p>
			   	<b>2. VETO:</b> si el Pretor escoge una carta amarilla debe
				mostrarla al resto de jugadores, y el Edil correspondiente la
				sustituye boca abajo por uno de sus otros dos votos.
			   	</p>
			   	<p>
			   	<b>3. CONTEO:</b> El voto amarillo es un voto nulo, y no hace
				avanzar ningún marcador de facción.
			   	</p>
			   	<p>
			   	<b>4. FIN DE TURNO:</b> El Cónsul descarta, boca abajo, una de
				sus cartas de facción a su elección. Sólo el jugador inicial
				realizará este paso, ya que el resto de jugadores tienen
				ya una sola carta de facción. Sólo se entrega la carta de
				rol de Cónsul, el resto de cartas se asignan al inicio del paso
				VOTACIÓN del próximo turno.
			   	</p>
		   	</ul>
   	</div>
   	
   	<div class="row">
	   	<h3>Final de la partida</h3>
		<p>
		   	La partida termina inmediatamente cuando se cumple una de las condiciones siguientes:
	   	</p>
	   	<ul>
		   	<p>
			<b>CONSPIRACIÓN FALLIDA:</b> Un marcador de facción
			sobrepasa el límite de jugadores, tal y como está indicado
			en la carta de “Svffragivm”.
		   	</p>
		   	<p>
			<b>IDUS DE MARZO:</b> El jugador inicial recibe la carta de rol
			de Cónsul por tercera vez.
		   	</p>
	   	</ul>
	   	En cualquier caso, cada jugador revela su carta de facción y se determina la facción ganadora:
	   	<ul>
		   	<p>
		   	En caso de <b>CONSPIRACIÓN FALLIDA</b>, la facción cuyo
			marcador sobrepase el límite de jugadores es descubierta
			por la facción rival y sus integrantes son asesinados. En
			consecuencia, gana la facción rival.
		   	</p>
		   	<p>
		   	En caso de <b>IDUS DE MARZO</b>, la facción que tenga 2 o más
			votos que la facción rival gana la partida ya que consigue
			frustrar los planes de sus oponentes.
		   	</p>
		   	<p>
		   	En caso que no hubiera <b>ningún jugador de la facción rival</b>,
			gana la facción “Mercader”.
		   	</p>
		   	<p>
		   	En caso contrario <b>(igualdad de votos, diferencia de 1 voto
			o no hubiera ningún jugador de la facción rival)</b>, gana la
			facción “Mercader” ya que han conseguido mantener el
			status quo...hasta los próximos Idus de Marzo, claro está.
		   	</p>
	   	</ul>
    </div>
</petclinic:layout>
