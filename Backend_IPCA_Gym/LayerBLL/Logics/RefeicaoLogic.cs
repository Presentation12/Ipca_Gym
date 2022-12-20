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
    public class RefeicaoLogic
    {
        public static async Task<Response> GetRefeicoesLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Refeicao> refeicaoList = await LayerDAL.RefeicaoService.GetRefeicoesService(sqlDataSource);
            if (refeicaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(refeicaoList);
            }
            return response;
        }
    }
}