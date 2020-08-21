package br.com.araujo.rastreabilidade.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.Person;

public class InetOrgPersonAraujo extends Person {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private String employeeID;
	private String department;
	private List<String> departmentNumber = new ArrayList<>();
	private String displayName;
	private String mail;
	
	public String getDepartment() {
		return department;
	}

	public String getEmployeeID() {
		return employeeID;
	}
	
	public String getMail() {
		return mail;
	}

	public String[] getDepartmentNumber() {
		return departmentNumber.toArray(new String[0]);
	}

	public String getDisplayName() {
		return displayName;
	}

	protected void populateContext(DirContextAdapter adapter) {
		super.populateContext(adapter);
		adapter.setAttributeValue("employeeID", employeeID);
		adapter.setAttributeValue("departmentNumber", departmentNumber);
		adapter.setAttributeValue("displayName", displayName);
		adapter.setAttributeValue("mail", mail);
		adapter.setAttributeValues("objectclass", new String[] { "top", "person",
				"organizationalPerson", "inetOrgPersonAraujo", "physicalDeliveryOfficeName"});
	}

	public static class Essence extends Person.Essence {
		public Essence() {
		}

		public Essence(InetOrgPersonAraujo copyMe) {
			super(copyMe);
			setEmployeeID(copyMe.getEmployeeID());
			setDepartment(copyMe.getDepartment());
			setDepartmentNumber(copyMe.getDepartmentNumber());
			setDisplayName(copyMe.getDisplayName());
			setMail(copyMe.getMail());
			
			((InetOrgPersonAraujo) instance).departmentNumber = new ArrayList<>(copyMe.departmentNumber);
		}

		public Essence(DirContextOperations ctx) {
			super(ctx);			
			setEmployeeID(ctx.getStringAttribute("employeeID"));
			setDepartment(ctx.getStringAttribute("department"));
			setDepartmentNumber(ctx.getStringAttributes("departmentNumber"));
			setDisplayName(ctx.getStringAttribute("displayName"));
			setMail(ctx.getStringAttribute("mail"));
		}

		protected LdapUserDetailsImpl createTarget() {
			return new InetOrgPersonAraujo();
		}

		public void setEmployeeID(String no) {
			((InetOrgPersonAraujo) instance).employeeID = no;
		}
		
		public void setDepartment(String no) {
			((InetOrgPersonAraujo) instance).department = no;
		}
		
		public void setMail(String email) {
			((InetOrgPersonAraujo) instance).mail = email;
		}
		
		public void setDepartmentNumber(String[] departmentNumber) {
			if (departmentNumber != null) {				
				((InetOrgPersonAraujo) instance).departmentNumber = Arrays.asList(departmentNumber);
			}
		}

		public void setDisplayName(String displayName) {
			((InetOrgPersonAraujo) instance).displayName = displayName;
		}
	}
}

