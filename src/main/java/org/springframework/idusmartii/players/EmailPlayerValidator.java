//package org.springframework.idusmartii.players;
//
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class EmailPlayerValidator implements ConstraintValidator<EmailConstraint, String>{
//
//	
//	@Autowired
//	private PlayerService playerService;
//	
//	@Override
//	public void initialize(EmailConstraint emailConstraint) {
//		
//	}
//	
//	
//	@Override
//	public boolean isValid(String value, ConstraintValidatorContext context) {
//		String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
//		Iterable<Player> playerIterable = playerService.findAll();
//		Stream<Player> players= IteratorToStream.iterableToStream(playerIterable);
//		boolean isAEmail = Pattern.compile(regexEmail).matcher(value).matches();
//		boolean noSameEmail = players.filter(x->x.getEmail().trim().equals(value)).collect(Collectors.toList()).size()==0;
//		boolean res = value!=null && value.length()>5 && value.length()<50 && noSameEmail && isAEmail;
//		boolean res1 = res;
//		return res1;
//	}
//	
//}
