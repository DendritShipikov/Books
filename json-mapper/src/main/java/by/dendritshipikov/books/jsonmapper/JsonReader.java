package by.dendritshipikov.books.jsonmapper;

import by.dendritshipikov.books.jsonmapper.builders.*;

public class JsonReader {
    private final String source;
    private int count = 0;

    public JsonReader(String source) {
        this.source = source;
    }

    private void ignoreSpace() {
        while (count < source.length() && Character.isWhitespace(source.charAt(count))) ++count;
    }

    private char peekChar() {
        if (count < source.length())
            return source.charAt(count);
        else
            return 0;
    }

    private char getChar() {
        if (count < source.length())
            return  source.charAt(count++);
        else
            return 0;
    }

    private String getChars(int n) {
        int m = Integer.min(n, source.length() - count);
        String str = source.substring(count, count + m);
        count += m;
        return str;
    }

    private String peekChars(int n) {
        int m = Integer.min(n, source.length() - count);
        String str = source.substring(count, count + m);
        return str;
    }

    private void matchChar(char ch) {
        ignoreSpace();
        if (peekChar() != ch) throw new JsonException("Parsing error: '" + ch + "' expected");
        getChar();
    }

    private boolean checkChar(char ch) {
        ignoreSpace();
        if (peekChar() != ch) return false;
        getChar();
        return true;
    }

    private boolean checkNull() {
        ignoreSpace();
        String str = peekChars(4);
        if (!str.equals("null")) return false;
        count += 4;
        return true;
    }

    private String readKeyword() {
        StringBuilder stringBuilder = new StringBuilder();
        while (Character.isLetter(peekChar())) stringBuilder.append(getChar());
        return stringBuilder.toString();
    }

    public Object readArray(JsonArrayBuilder arrayBuilder) {
        if (checkNull()) return null;
        arrayBuilder.reset();
        JsonBuilder componentBuilder = arrayBuilder.getComponentBuilder();
        matchChar('[');
        ignoreSpace();
        if (peekChar() != ']') {
            do {
                Object e = componentBuilder.build(this);
                arrayBuilder.add(e);
            } while (checkChar(','));
        }
        matchChar(']');
        return arrayBuilder.getResult();
    }

    public Object readObject(JsonObjectBuilder objectBuilder) {
        if (checkNull()) return null;
        JsonBuilder stringBuilder = JsonMapper.getBuilder(String.class);
        objectBuilder.reset();
        matchChar('{');
        ignoreSpace();
        if (peekChar() != '}') {
            do {
                stringBuilder.reset();
                String name = (String) stringBuilder.build(this);
                if (objectBuilder.containsField(name)) throw new JsonException("Multiple keys in object");
                matchChar(':');
                JsonBuilder fieldBuilder = objectBuilder.getFieldBuilder(name);
                Object value = fieldBuilder.build(this);
                objectBuilder.put(name, value);
            } while (checkChar(','));
        }
        matchChar('}');
        return objectBuilder.getResult();
    }

    public Object readString(JsonStringBuilder stringBuilder) {
        if (checkNull()) return null;
        stringBuilder.reset();
        this.matchChar('"');
        while (true) {
            char current = this.peekChar();
            if (current < '\u0020') throw new JsonException("Parsing error: wrong symbol \\0x" + Integer.toHexString((int)current) + " in string");
            switch (current) {
                case '\\':
                    this.getChar();
                    switch (this.peekChar()) {
                        case '\\':
                            stringBuilder.append('\\');
                            break;
                        case '/':
                            stringBuilder.append('/');
                            break;
                        case '"':
                            stringBuilder.append('"');
                            break;
                        case 'n':
                            stringBuilder.append('\n');
                            break;
                        case 'r':
                            stringBuilder.append('\r');
                            break;
                        case 'f':
                            stringBuilder.append('\f');
                            break;
                        case 't':
                            stringBuilder.append('\t');
                            break;
                        case 'u':
                            this.getChar();
                            String hex = this.getChars(4);
                            if (hex.length() != 4) throw new JsonException("Parsing error: wrong escape sequence format");
                            int code;
                            try {
                                code = Integer.parseUnsignedInt(hex, 16);
                            }
                            catch (NumberFormatException e) {
                                throw new JsonException("Parsing error: wrong escape sequence format");
                            }
                            stringBuilder.append((char)code);
                            break;
                        default:
                            throw new JsonException("Parsing error: wrong escape sequence format");
                    }
                    this.getChar();
                    break;
                case '"':
                    this.getChar();
                    return stringBuilder.getResult();
                default:
                    stringBuilder.append(current);
                    this.getChar();
                    break;
            }
        }
    }

    public Object readNumber(JsonNumberBuilder numberBuilder) {
        if (checkNull()) return null;
        StringBuilder builder = new StringBuilder();
        if (this.peekChar() == '-') builder.append(this.getChar());
        if (this.peekChar() == '0') {
            builder.append(this.getChar());
        } else if (Character.isDigit(this.peekChar())) {
            while (Character.isDigit(this.peekChar())) builder.append(this.getChar());
        } else {
            throw new JsonException("Parsing error: wrong number format");
        }
        if (this.peekChar() == '.') {
            builder.append(this.getChar());
            while (Character.isDigit(this.peekChar())) builder.append(this.getChar());
        }
        if (this.peekChar() == 'e' || this.peekChar() == 'E') {
            builder.append(this.getChar());
            if (this.peekChar() == '+' || this.peekChar() == '-') builder.append(this.getChar());
            if (!Character.isDigit(this.peekChar())) throw new JsonException("Parsing error: wrong number format");
            while (Character.isDigit(this.peekChar())) builder.append(this.getChar());
        }
        String rep = builder.toString();
        numberBuilder.setValue(rep);
        return numberBuilder.getResult();
    }

    public Object readBool(JsonBoolBuilder boolBuilder) {
        if (checkNull()) return null;
        String rep = readKeyword();
        boolean value;
        if (rep.equals("true")) value = true;
        else if (rep.equals("false")) value = false;
        else throw new JsonException("'true' or 'false' expected");
        boolBuilder.setValue(value);
        return boolBuilder.getResult();
    }
}
