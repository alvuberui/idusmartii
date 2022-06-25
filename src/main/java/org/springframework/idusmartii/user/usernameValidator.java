//package org.springframework.idusmartii.user;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.idusmartii.players.IteratorToStream;
//import org.springframework.idusmartii.players.Player;
//import org.springframework.idusmartii.players.PlayerService;
//
//
//public class usernameValidator implements ConstraintValidator<usernameConstraint, String>{
//
//	@Autowired
//	private PlayerService playerService;
//	
//	@Override
//	public void initialize(usernameConstraint usernameConstraint) {
//	}
//	
//	
//	@Override
//	public boolean isValid(String value, ConstraintValidatorContext context) {
//		Iterable<Player> playersIterable = playerService.findAll();
//		boolean noSameUsername = IteratorToStream.iterableToStream(playersIterable).anyMatch(x->x.getUser().getUsername().toLowerCase().equals(value.toLowerCase()));
//		boolean res = noSameUsername;
//		return res;
//	}
//
//}
