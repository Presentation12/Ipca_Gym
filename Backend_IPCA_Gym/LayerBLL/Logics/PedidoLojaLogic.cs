using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;


namespace LayerBLL.Logics
{
    public class PedidoLojaLogic
    {
        public static async Task<Response> GetPedidoLojasLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PedidoLoja> pedidolojaList = await PedidoLojaService.GetPedidoLojasService(sqlDataSource);

            if (pedidolojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(pedidolojaList);
            }

            return response;
        }
    }
}
