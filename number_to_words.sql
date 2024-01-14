DELIMITER $$
CREATE FUNCTION `number_to_words`(n INT) RETURNS varchar(100)
BEGIN
    -- This function returns the string representation of a number.
    -- It's just an example... I'll restrict it to hundreds, but
    -- it can be extended easily.
    -- The idea is:
    --      For each digit you need a position,
    --      For each position, you assign a string
    declare ans varchar(100);
    declare dig1, dig2, dig3, dig4, dig5, dig6 int;

    set ans = '';

    set dig6 = CAST(RIGHT(CAST(floor(n / 100000) as CHAR(8)), 1) as SIGNED);
    set dig5 = CAST(RIGHT(CAST(floor(n / 10000) as CHAR(8)), 1) as SIGNED);
    set dig4 = CAST(RIGHT(CAST(floor(n / 1000) as CHAR(8)), 1) as SIGNED);
    set dig3 = CAST(RIGHT(CAST(floor(n / 100) as CHAR(8)), 1) as SIGNED);
    set dig2 = CAST(RIGHT(CAST(floor(n / 10) as CHAR(8)), 1) as SIGNED);
    set dig1 = CAST(RIGHT(floor(n), 1) as SIGNED);

    if dig6 > 0 then
        case
            when dig6=1 then set ans=concat(ans, 'one hundred');
            when dig6=2 then set ans=concat(ans, 'two hundred');
            when dig6=3 then set ans=concat(ans, 'three hundred');
            when dig6=4 then set ans=concat(ans, 'four hundred');
            when dig6=5 then set ans=concat(ans, 'five hundred');
            when dig6=6 then set ans=concat(ans, 'six hundred');
            when dig6=7 then set ans=concat(ans, 'seven hundred');
            when dig6=8 then set ans=concat(ans, 'eight hundred');
            when dig6=9 then set ans=concat(ans, 'nine hundred');
            else set ans = ans;
            end case;
    end if;

    if dig5 = 1 then
        case
            when (dig5*10 + dig4) = 10 then set ans=concat(ans, ' ten thousand ');
            when (dig5*10 + dig4) = 11 then set ans=concat(ans, ' eleven thousand ');
            when (dig5*10 + dig4) = 12 then set ans=concat(ans, ' twelve thousand ');
            when (dig5*10 + dig4) = 13 then set ans=concat(ans, ' thirteen thousand ');
            when (dig5*10 + dig4) = 14 then set ans=concat(ans, ' fourteen thousand ');
            when (dig5*10 + dig4) = 15 then set ans=concat(ans, ' fifteen thousand ');
            when (dig5*10 + dig4) = 16 then set ans=concat(ans, ' sixteen thousand ');
            when (dig5*10 + dig4) = 17 then set ans=concat(ans, ' seventeen thousand ');
            when (dig5*10 + dig4) = 18 then set ans=concat(ans, ' eighteen thousand ');
            when (dig5*10 + dig4) = 19 then set ans=concat(ans, ' nineteen thousand ');
            else set ans=ans;
            end case;
    else
        if dig5 > 0 then
            case
                when dig5=2 then set ans=concat(ans, ' twenty');
                when dig5=3 then set ans=concat(ans, ' thirty');
                when dig5=4 then set ans=concat(ans, ' fourty');
                when dig5=5 then set ans=concat(ans, ' fifty');
                when dig5=6 then set ans=concat(ans, ' sixty');
                when dig5=7 then set ans=concat(ans, ' seventy');
                when dig5=8 then set ans=concat(ans, ' eighty');
                when dig5=9 then set ans=concat(ans, ' ninety');
                else set ans=ans;
                end case;
        end if;
        if dig4 > 0 then
            case
                when dig4=1 then set ans=concat(ans, ' one thousand ');
                when dig4=2 then set ans=concat(ans, ' two thousand ');
                when dig4=3 then set ans=concat(ans, ' three thousand ');
                when dig4=4 then set ans=concat(ans, ' four thousand ');
                when dig4=5 then set ans=concat(ans, ' five thousand ');
                when dig4=6 then set ans=concat(ans, ' six thousand ');
                when dig4=7 then set ans=concat(ans, ' seven thousand ');
                when dig4=8 then set ans=concat(ans, ' eight thousand ');
                when dig4=9 then set ans=concat(ans, ' nine thousand ');
                else set ans=ans;
                end case;
        end if;
        if dig4 = 0 AND (dig5 != 0 || dig6 != 0) then
            set ans=concat(ans, ' thousand ');
        end if;
    end if;

    if dig3 > 0 then
        case
            when dig3=1 then set ans=concat(ans, 'one hundred');
            when dig3=2 then set ans=concat(ans, 'two hundred');
            when dig3=3 then set ans=concat(ans, 'three hundred');
            when dig3=4 then set ans=concat(ans, 'four hundred');
            when dig3=5 then set ans=concat(ans, 'five hundred');
            when dig3=6 then set ans=concat(ans, 'six hundred');
            when dig3=7 then set ans=concat(ans, 'seven hundred');
            when dig3=8 then set ans=concat(ans, 'eight hundred');
            when dig3=9 then set ans=concat(ans, 'nine hundred');
            else set ans = ans;
            end case;
    end if;

    if dig2 = 1 then
        case
            when (dig2*10 + dig1) = 10 then set ans=concat(ans, ' ten');
            when (dig2*10 + dig1) = 11 then set ans=concat(ans, ' eleven');
            when (dig2*10 + dig1) = 12 then set ans=concat(ans, ' twelve');
            when (dig2*10 + dig1) = 13 then set ans=concat(ans, ' thirteen');
            when (dig2*10 + dig1) = 14 then set ans=concat(ans, ' fourteen');
            when (dig2*10 + dig1) = 15 then set ans=concat(ans, ' fifteen');
            when (dig2*10 + dig1) = 16 then set ans=concat(ans, ' sixteen');
            when (dig2*10 + dig1) = 17 then set ans=concat(ans, ' seventeen');
            when (dig2*10 + dig1) = 18 then set ans=concat(ans, ' eighteen');
            when (dig2*10 + dig1) = 19 then set ans=concat(ans, ' nineteen');
            else set ans=ans;
            end case;
    else
        if dig2 > 0 then
            case
                when dig2=2 then set ans=concat(ans, ' twenty');
                when dig2=3 then set ans=concat(ans, ' thirty');
                when dig2=4 then set ans=concat(ans, ' fourty');
                when dig2=5 then set ans=concat(ans, ' fifty');
                when dig2=6 then set ans=concat(ans, ' sixty');
                when dig2=7 then set ans=concat(ans, ' seventy');
                when dig2=8 then set ans=concat(ans, ' eighty');
                when dig2=9 then set ans=concat(ans, ' ninety');
                else set ans=ans;
                end case;
        end if;
        if dig1 > 0 then
            case
                when dig1=1 then set ans=concat(ans, ' one');
                when dig1=2 then set ans=concat(ans, ' two');
                when dig1=3 then set ans=concat(ans, ' three');
                when dig1=4 then set ans=concat(ans, ' four');
                when dig1=5 then set ans=concat(ans, ' five');
                when dig1=6 then set ans=concat(ans, ' six');
                when dig1=7 then set ans=concat(ans, ' seven');
                when dig1=8 then set ans=concat(ans, ' eight');
                when dig1=9 then set ans=concat(ans, ' nine');
                else set ans=ans;
                end case;
        end if;
    end if;

    return trim(ans);
END
$$