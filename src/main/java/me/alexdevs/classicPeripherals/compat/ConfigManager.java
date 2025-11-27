package me.alexdevs.classicPeripherals.compat;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Supplier;

public class ConfigManager {
    public static <T> T createToml(Path directory, String id, Class<T> classOfT, Supplier<T> creator) {
        var path = directory.resolve(String.format("%s.toml", id));
        path.toFile().getParentFile().mkdirs();

        if (!path.toFile().exists()) {
            var obj = creator.get();
            var str = serialize(obj);

            try (var writer = new FileWriter(path.toFile())) {
                writer.write(str);
            } catch (IOException e) {
                System.err.println(e.toString());
            }

            return obj;
        }

        try (var reader = new BufferedReader(new FileReader(path.toFile()))) {
            return deserialize(reader, classOfT);
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.err.println(e.toString());
            return creator.get();
        }
    }

    private static <T> T deserialize(BufferedReader reader, Class<T> classOfT) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var map = new HashMap<String, String>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#") || line.isBlank())
                continue;

            var parts = line.split("=", 2);
            if (parts.length == 2) {
                map.put(parts[0].trim(), parts[1].trim());
            }
        }

        T inst = classOfT.getConstructor().newInstance();
        for (var field : classOfT.getFields()) {
            if(map.containsKey(field.getName())) {
                var value = parseValue(field.getType(), map.get(field.getName()));
                field.set(inst, value);
            }
        }

        return inst;
    }

    private static <T> String serialize(T obj) {
        var c = obj.getClass();

        var builder = new StringBuilder();
        for (var field : c.getFields()) {
            String value;
            try {
                value = field.get(obj).toString();
            } catch (IllegalAccessException e) {
                continue;
            }
            builder.append(getComment(field, value));
            var name = field.getName();
            builder.append(String.format("%s = %s\n", name, value));
        }
        return builder.toString();
    }

    private static String getComment(Field field, String defaultValue) {
        var comment = field.getAnnotationsByType(Comment.class);
        var floatRange = field.getAnnotationsByType(FloatRange.class);
        var intRange = field.getAnnotationsByType(IntegerRange.class);

        var builder = new StringBuilder();
        for (var c : comment) {
            builder.append(String.format("# %s\n", c.value()));
        }

        if (floatRange.length == 1) {
            var min = floatRange[0].min();
            var max = floatRange[0].max();
            builder.append(String.format("# range: %.1f - %.1f\n", min, max));
        }

        if (intRange.length == 1) {
            var min = intRange[0].min();
            var max = intRange[0].max();
            builder.append(String.format("# range: %d - %d\n", min, max));
        }

        builder.append(String.format("# default: %s\n", defaultValue));

        return builder.toString();
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == String.class) {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            return value;
        }
        throw new RuntimeException("Unsupported field type: " + type.getName());
    }

}
