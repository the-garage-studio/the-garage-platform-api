package the.garage.platform.shared.domain.services;

import java.security.SecureRandom;

/**
 * Generador centralizado de códigos de verificación numéricos de 8 dígitos.
 *
 * <p>Se extrae aquí para evitar duplicar la lógica entre los servicios de
 * verificación de email y restablecimiento de contraseña, y para garantizar
 * que en ambos flujos se use {@link SecureRandom} en lugar de {@link java.util.Random},
 * ya que los códigos tienen implicaciones de seguridad (acceso a la cuenta y
 * cambio de contraseña).</p>
 */
public final class VerificationCodeGenerator {

    /** Un único {@link SecureRandom} compartido es thread-safe y costoso de inicializar. */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /** El módulo que garantiza exactamente 8 dígitos decimales (0–99 999 999). */
    private static final int MODULUS = 100_000_000;

    private VerificationCodeGenerator() {
        // Clase utilitaria; no debe instanciarse.
    }

    /**
     * Genera un código numérico de exactamente 8 dígitos, con relleno de ceros
     * a la izquierda si el valor aleatorio es inferior a 10 000 000.
     *
     * <p>Ejemplo de salida: {@code "00482913"}, {@code "73291840"}.</p>
     *
     * @return código de 8 dígitos como {@link String}
     */
    public static String generate() {
        int code = SECURE_RANDOM.nextInt(MODULUS);
        // %08d garantiza que siempre se emitan exactamente 8 dígitos.
        return String.format("%08d", code);
    }
}
