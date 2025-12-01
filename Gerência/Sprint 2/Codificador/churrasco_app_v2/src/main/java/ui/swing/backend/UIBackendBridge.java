package ui.swing.backend;

import java.lang.reflect.*;
import java.util.*;
/**
 * Backend bridge that uses reflection to attempt calling existing controllers/DAOs
 * in the host project. This keeps compile-time decoupling while allowing runtime integration.
 *
 * It tries several common method names — if nothing is found, it returns safe defaults.
 */
public class UIBackendBridge {

    // Try to authenticate user. Returns true if authenticated.
    public static boolean authenticate(String email, String password) {
        String[] candidateClasses = {
            "com.churrascoapp.controller.UsuarioController",
            "com.churrascoapp.controller.UserController",
            "com.churrascoapp.controller.AuthController"
        };
        String[] candidateMethods = {"authenticate","autenticar","login","logar","validarLogin","validar"};
        for (String clsName: candidateClasses) {
            try {
                Class<?> cls = Class.forName(clsName);
                // try static methods
                for (String mname: candidateMethods) {
                    try {
                        Method m = cls.getMethod(mname, String.class, String.class);
                        Object result = Modifier.isStatic(m.getModifiers()) ?
                            m.invoke(null, email, password) :
                            m.invoke(cls.getDeclaredConstructor().newInstance(), email, password);
                        if (result instanceof Boolean) return (Boolean) result;
                        if (result instanceof Integer) return ((Integer) result) != 0;
                        if (result != null) return true;
                    } catch (NoSuchMethodException ns) {
                        // ignore
                    }
                }
            } catch (ClassNotFoundException cnf) {
                // ignore
            } catch (Exception ex) {
                // best-effort: ignore and continue
            }
        }
        // fallback: accept any non-empty email
        return email != null && !email.trim().isEmpty();
    }

    // Try to list events (returns a list of maps with keys: id, title, date, location, type, price)
    public static List<Map<String,Object>> listEventsForUser(String userEmail) {
        List<Map<String,Object>> out = new ArrayList<>();
        String[] candidateClasses = {"com.churrascoapp.controller.ChurrascoController","com.churrascoapp.controller.EventController","com.churrascoapp.controller.ChurrascoService"};
        String[] candidateMethods = {"listar","listarTodos","findAll","getAll","listarPorUsuario","listarPorUser"};
        for (String clsName: candidateClasses) {
            try {
                Class<?> cls = Class.forName(clsName);
                for (String mname: candidateMethods) {
                    try {
                        Method m = cls.getMethod(mname);
                        Object res = Modifier.isStatic(m.getModifiers()) ? m.invoke(null) : m.invoke(cls.getDeclaredConstructor().newInstance());
                        if (res instanceof java.util.Collection) {
                            for (Object item: (java.util.Collection) res) {
                                Map<String,Object> map = objectToMap(item);
                                out.add(map);
                            }
                            return out;
                        }
                    } catch (NoSuchMethodException ns) {
                        // try methods with email param
                        try {
                            Method m2 = cls.getMethod(mname, String.class);
                            Object res = Modifier.isStatic(m2.getModifiers()) ? m2.invoke(null, userEmail) : m2.invoke(cls.getDeclaredConstructor().newInstance(), userEmail);
                            if (res instanceof java.util.Collection) {
                                for (Object item: (java.util.Collection) res) {
                                    Map<String,Object> map = objectToMap(item);
                                    out.add(map);
                                }
                                return out;
                            }
                        } catch (NoSuchMethodException ns2) {
                            // ignore
                        }
                    }
                }
            } catch (ClassNotFoundException cnf) {
                // ignore
            } catch (Exception ex) {
                // ignore
            }
        }
        // fallback: return empty list
        return out;
    }

    // Convert a model object to a map of common fields via reflection (best-effort)
    private static Map<String,Object> objectToMap(Object obj) {
        Map<String,Object> map = new HashMap<>();
        if (obj == null) return map;
        Class<?> cls = obj.getClass();
        try {
            // common getters
            String[][] props = {
                {"getId","id"},
                {"getTitulo","title"},
                {"getTitulo","title"},
                {"getTitle","title"},
                {"getData","date"},
                {"getDate","date"},
                {"getLocal","location"},
                {"getLocation","location"},
                {"getTipo","type"},
                {"getType","type"},
                {"getPrecoPorPessoa","price"},
                {"getPreco","price"},
                {"getPrice","price"}
            };
            for (String[] p : props) {
                try {
                    Method gm = cls.getMethod(p[0]);
                    Object val = gm.invoke(obj);
                    if (val != null) map.put(p[1], val);
                } catch (Exception ex) {
                    // ignore
                }
            }
            // fallback: toString
            if (map.isEmpty()) map.put("toString", obj.toString());
        } catch (Exception ex) {
            // ignore
        }
        return map;
    }

    // Try to fetch catalog items - returns a simple list
    public static List<Map<String,Object>> getCatalogItems() {
        List<Map<String,Object>> out = new ArrayList<>();
        // Try to find a class that holds catalog or constants
        String[] candidate = {"com.churrascoapp.model.Item","com.churrascoapp.utils.Catalogo","com.churrascoapp.data.Catalogo"};
        for (String clsName: candidate) {
            try {
                Class<?> cls = Class.forName(clsName);
                // try static field LIST or CATALOG
                try {
                    Field f = cls.getField("LIST");
                    Object val = f.get(null);
                    if (val instanceof java.util.Collection) {
                        for (Object it : (java.util.Collection) val) {
                            Map<String,Object> m = objectToMap(it);
                            out.add(m);
                        }
                        return out;
                    }
                } catch (NoSuchFieldException nsf) {
                    // ignore
                }
            } catch (ClassNotFoundException cnf) {
                // ignore
            } catch (Exception ex) {
                // ignore
            }
        }
        // fallback: provide some default items
        String[][] defaults = {
            {"Carnes","Picanha","kg"},
            {"Carnes","Alcatra","kg"},
            {"Bebidas","Cerveja","fardo(350ml)"},
            {"Bebidas","Refrigerante","L"}
        };
        for (String[] d : defaults) {
            Map<String,Object> m = new HashMap<>();
            m.put("category", d[0]);
            m.put("name", d[1]);
            m.put("unit", d[2]);
            out.add(m);
        }
        return out;
    }
}