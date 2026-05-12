package com.identity.dbservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.identity.entity.AuditTrail;
import com.identity.entity.District;
import com.identity.entity.Users;
import com.identity.repository.AuditRepository;
import com.identity.repository.UsersRepository;
import com.vaadin.flow.server.VaadinRequest;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuditService {
	private UsersRepository uRepo;
	private AuditRepository aRepo;
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	

	public AuditService(AuditRepository aRepo, UsersRepository uRepo) {
		this.aRepo = aRepo;
		this.uRepo = uRepo;
	}

	public void updateAudit(AuditTrail entity) {
		aRepo.save(entity);
	}

	public List<AuditTrail> getAuditTrail(District district) {
		//return aRepo.findByDistrictOrderByIdDesc(district);
		return aRepo.findAllByOrderByIdDesc();
	}

	public String getRealClientIp() {
		VaadinRequest request = VaadinRequest.getCurrent();
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
		if (xForwardedForHeader == null || xForwardedForHeader.isEmpty()) {
			return request.getRemoteAddr();
		} else {
			return xForwardedForHeader.split(",")[0].trim();
		}
	}

	public String getRealClientIp(HttpServletRequest request) {
		if (request == null)
			return "N/A";

		String xff = request.getHeader("X-Forwarded-For");
		if (xff == null || xff.isBlank()) {
			return request.getRemoteAddr();
		}
		return xff.split(",")[0].trim();
	}

	

	
	public void saveAudit(String action, String process, String details, String otherDetails) {
		AuditTrail audit;
		audit = new AuditTrail();
		audit.setAction(action);
		audit.setProcess(process);
		audit.setOtherDetails(otherDetails);
		audit.setDetails(details);
		audit.setActionOn(LocalDateTime.now());
		audit.setActionBy(getLoggedUser());
		audit.setIpAddress(getRealClientIp());
		audit.setDistrict(getLoggedUser().getDistrict());
		updateAudit(audit);
	}

	public void saveAuthAudit(String action, String process, String username, String odetails, String ip) {
		AuditTrail audit = new AuditTrail();
		audit.setAction(action); // LOGIN_SUCCESS / LOGIN_FAIL / LOGOUT / EXPIRED
		audit.setProcess(process);
		audit.setDetails("User=" + username);
		audit.setOtherDetails(odetails);
		audit.setIpAddress(ip);
		audit.setActionOn(LocalDateTime.now());
		updateAudit(audit);
	}

	public Users getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// No auth or not authenticated
		if (auth == null || !auth.isAuthenticated()) {
			return null; // or throw IllegalStateException if you prefer
		}

		// IMPORTANT: anonymous is considered "authenticated" in Spring
		if (auth instanceof AnonymousAuthenticationToken) {
			return null;
		}

		Object principal = auth.getPrincipal();

		// Resolve username safely
		String username;
		if (principal instanceof UserDetails ud) {
			username = ud.getUsername();
		} else if (principal instanceof String s) {
			// This covers cases like "anonymousUser"
			if ("anonymousUser".equalsIgnoreCase(s)) {
				return null;
			}
			username = s;
		} else {
			// Unknown principal type
			return null;
		}

		Users user = uRepo.findByUserNameAndEnabled(username, true);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
		return user;
	}
}
