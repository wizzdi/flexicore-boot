package com.wizzdi.security.bearer.jwt;

import javax.crypto.SecretKey;

public record SecretKeyHolder(SecretKey secretKey) {
}
