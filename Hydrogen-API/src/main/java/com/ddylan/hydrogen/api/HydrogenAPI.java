package com.ddylan.hydrogen.api;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HydrogenAPI extends SpringBootServletInitializer {

/*

	GET
	/ranks
	/prefixes
	/serverGroups
	/servers
	/chatFilter
	/whoami
	/prefixes/grants?user={uuid}
	/grants?user={uuid}
	/users/{uuid}/details
	/users/{uuid}/requiresTotp/{userIp}
	/dumps/totp
	/punishments?user={uuid}

	POST
	/users/{uuid}/colors?iconColor={nameColor}
	/users/{uuid}/registerEmail?email={email}?userIp={userIp}
	/users/{uuid}/verifyTotp?totpCode={code}?userIp={userIp}
	/users/{uuid}/prefix?prefix={prefix}
	/users/{uuid}/setupTotp?userIp={userIp}?secret={secret}?totpCode{code}
	/users/{uuid}/login?user={username}?userIp={userIp}
	/disposableLoginTokens?user={uuid}?userIp={userIp}
	/grants?body={grant}
	/prefixes?body={grant}
	/punishments?body={punishment}
	/servers/heartbeat{players} {lastTps} {events} {permissionsNeeded}


	DELETE
	/grants/{id} {removedBy} {removedByIp} {reason}
	/prefixes/{id} {removedBy} {removedByIp} {reason}
	/users/{uuid}/activePunishment {query}

 */


	@Getter
	private static SettingsManager settingsManager = new SettingsManager();
	@Getter
	private static RedisManager redisManager = new RedisManager();

	public static void main(String[] args) {
		if (!settingsManager.init(true) || !redisManager.init()) {
			return;
		}
		SpringApplication.run(HydrogenAPI.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HydrogenAPI.class);
	}
}
