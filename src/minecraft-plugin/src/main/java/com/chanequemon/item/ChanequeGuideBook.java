package com.chanequemon.item;

import com.chanequemon.model.Creature;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChanequeGuideBook {
    public static ItemStack create(Collection<Creature> creatures) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("Guia de Crianza de Chanequemon");
        meta.setAuthor("Lemegeton Clavicula Salomonis");

        List<String> pages = new ArrayList<>();
        pages.add("\u00A76\u00A7lGu\u00EDa de Crianza\n\u00A78de\n\u00A75Chanequemon\n\n\u00A77Compendio de h\u00E1bitats,\ncondiciones y secretos\npara encontrar y capturar\ncriaturas mitol\u00F3gicas.\n\n\u00A78\u00A7oUsa este saber\ncon responsabilidad.");

        for (Creature c : creatures) {
            if (!c.hasSpawnConditions()) continue;
            StringBuilder page = new StringBuilder();
            page.append("\u00A7b\u00A7l").append(c.displayName()).append("\n\u00A77").append(c.type().name()).append("\n\n");

            if (!c.spawnBiomes().isEmpty()) {
                page.append("\u00A76Biomas:\n\u00A77");
                List<String> biomes = c.spawnBiomes().stream().limit(4).toList();
                page.append(String.join(", ", biomes));
                if (c.spawnBiomes().size() > 4) page.append("...");
                page.append("\n");
            }

            if (!c.spawnStructures().isEmpty()) {
                page.append("\u00A76Estructuras:\n\u00A77");
                page.append(String.join(", ", c.spawnStructures()));
                page.append("\n");
            }

            String time = c.spawnTime();
            if (time != null && !"ANY".equals(time)) {
                page.append("\u00A76Tiempo: \u00A77").append(time).append("\n");
            }

            if (!c.spawnPotionEffects().isEmpty()) {
                page.append("\u00A6Pociones: \u00A77");
                page.append(String.join(", ", c.spawnPotionEffects()));
                page.append("\n");
            }

            if (!c.spawnPlayerEffects().isEmpty()) {
                page.append("\u00A76Efectos:\n\u00A77");
                page.append(String.join(", ", c.spawnPlayerEffects()));
                page.append("\n");
            }

            if (!c.spawnEnchantments().isEmpty()) {
                page.append("\u00A76Encatamientos:\n\u00A77");
                page.append(String.join(", ", c.spawnEnchantments()));
                page.append("\n");
            }

            if (c.badOmenMin() > 0) {
                page.append("\u00A76Mal Presagio: \u00A77Nv.").append(c.badOmenMin());
                if (c.badOmenMax() > c.badOmenMin()) page.append("-").append(c.badOmenMax());
                page.append("\n");
            }

            pages.add(page.toString());
        }

        meta.setPages(pages);
        book.setItemMeta(meta);
        return book;
    }
}
